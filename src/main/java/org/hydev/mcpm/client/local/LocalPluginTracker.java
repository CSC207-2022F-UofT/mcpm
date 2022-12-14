package org.hydev.mcpm.client.local;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.models.*;
import org.hydev.mcpm.client.models.PluginYml.InvalidPluginMetaStructure;
import org.hydev.mcpm.utils.Pair;
import org.hydev.mcpm.utils.PluginJarFile;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * This class keeps track of locally installed packages
 */
public class LocalPluginTracker implements PluginTracker
{
    /** Lock file containing installed plugin information */
    private final String mainLockFile;

    /** Directory containing all the plugins */
    private final String pluginDirectory;

    /** Json deserializer */
    final ObjectMapper mapper = new ObjectMapper();

    /**
     * Instantiates a LocalPluginTracker with default parameters for general use
     */
    public LocalPluginTracker() {
        this("plugins/mcpm.lock.json", "plugins");
    }

    /**
     * Instantiates a LocalPluginTracker with custom parameters for testing or other
     * purposes
     *
     * @param mainLockFile    The path to the main lock file
     * @param pluginDirectory The path to the plugin directory
     */
    public LocalPluginTracker(String mainLockFile, String pluginDirectory) {
        this.mainLockFile = mainLockFile;
        this.pluginDirectory = pluginDirectory;
    }

    /**
     * Read metadata from a plugin's jar
     *
     * @param jar Local plugin jar path
     * @return Metadata or null if it cannot be read
     */
    private static @Nullable PluginYml readMeta(File jar) {
        try (PluginJarFile InstancePluginJarFile = new PluginJarFile(jar)) {
            return InstancePluginJarFile.readPluginYaml();
        } catch (IOException | InvalidPluginMetaStructure e) {
            return null;
        }
    }

    /**
     * Read a JSON file at the given location file and return a mapping between
     * plugin names and install
     * status
     *
     */
    private ArrayList<PluginTrackerModel> readJson() {
        try {
            // Reads the json file and converts it to a list of PluginTrackerModel objects
            return this.mapper.readValue(new File(mainLockFile), new TypeReference<>() {});
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            throw new PluginTrackerError(e);
        }
    }

    /**
     * Save an ArrayList's contents into a JSON file by replacing all contents
     *
     * @param list ArrayList of PluginTrackerModel instances to save
     *
     */
    private void saveJson(ArrayList<PluginTrackerModel> list) {
        try {
            this.mapper.writeValue(Paths.get(mainLockFile).toFile(), list);
        } catch (IOException e) {
            throw new PluginTrackerError(e);
        }
    }

    /**
     * Create an index map of the json lock file
     *
     * @return Index map (dict[name: lock model])
     */
    private Map<String, PluginTrackerModel> mapLock() {
        return readJson().stream().map(it -> new Pair<>(it.getName(), it)).collect(Pair.toMap());
    }

    /**
     * Returns whether a plugin with the given name is within
     * the lock file
     *
     * @param name Name of the plugin
     * @return Whether the plugin is within the lock file
     */
    public Boolean findIfInLockByName(String name) {
        ArrayList<PluginTrackerModel> list = readJson();
        for (PluginTrackerModel pluginTrackerModel : list) {
            if (pluginTrackerModel.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a plugin to the JSON file
     *
     * @param name Plugin name
     * @param manual Plugin status (true = manual, false = auto)
     * @param pluginId Plugin unique ID
     * @param versionId Plugin version's unique ID
     */
    @Override
    public void addEntry(String name, boolean manual, long versionId, long pluginId) {
        ArrayList<PluginTrackerModel> currentList = this.readJson();
        currentList.add(new PluginTrackerModel(name, manual, versionId, pluginId));
        saveJson(currentList);
    }

    /**
     * Remove a plugin with the given name from the JSON file
     *
     * @param name Plugin name
     */
    @Override
    public void removeEntry(String name) {
        ArrayList<PluginTrackerModel> currentList = this.readJson();
        currentList.removeIf(pluginTrackerModel -> pluginTrackerModel.getName().equals(name));
        saveJson(currentList);
    }

    /**
     * List locally tracked plugin entries
     *
     * @return Entries
     */
    @Override
    public List<PluginTrackerModel> listEntries()
    {
        return readJson();
    }

    /**
     * List all currently installed plugins in an List
     */
    @Override
    public List<PluginYml> listInstalled() {
        // Go into the plugin directory and list files
        File dir = new File(pluginDirectory);
        File[] list = dir.listFiles();
        if (list == null)
            return new ArrayList<>();

        // Filter only java files, return all metadata that's not null
        return Arrays.stream(list).filter(f -> f.isFile() && f.getName().endsWith(".jar"))
            .parallel().map(LocalPluginTracker::readMeta).filter(Objects::nonNull).toList();
    }

    /**
     * Get a list of manually installed plugins. This should assume that everything that's installed
     * on the file system but not in the lock file is manually installed. (Maybe installed via other
     * means such as manually dragging the jar file into the directory).
     *
     * @return List of plugin names
     */
    @Override
    public List<String> listManuallyInstalled() {
        var lock = mapLock();

        // Manually installed plugins that are in the lock file
        var inLock = lock.values().stream().filter(PluginTrackerModel::isManual)
            .map(PluginTrackerModel::getName);

        // Installed plugins that are not in the lock file
        var noLock = listInstalled().stream().map(PluginYml::name)
            .filter(it -> !lock.containsKey(it));

        return Stream.concat(inLock, noLock).toList();
    }

    /**
     * List installed plugins in an index map
     *
     * @return Index map (dict[name: plugin])
     */
    private Map<String, PluginYml> mapInstalled() {
        return listInstalled().stream()
            .map(it -> new Pair<>(it.name(), it))
            .collect(Pair.toMap());
    }

    /**
     * Get a list of automatically installed plugin dependencies that are no longer required
     *
     * @param considerSoftDependencies Whether to preserve soft dependencies (should be false)
     * @return List of plugins
     */
    @Override
    public List<PluginYml> listOrphanPlugins(boolean considerSoftDependencies) {
        Map<String, PluginYml> installed = mapInstalled();
        List<String> manual = listManuallyInstalled();
        List<String> deps = new ArrayList<>();

        // Get all the dependencies of the manually installed plugins
        for (String name : manual) {

            // If the plugin is not in the local installed folder, skip it
            if (!installed.containsKey(name)) continue;

            // Read plugin yml
            var p = installed.get(name);

            // Add the dependencies of the plugin to the list of required dependencies
            if (p.depend() != null)
                deps.addAll(p.depend());

            // If considerSoftDependencies is true, add the soft dependencies to the list of
            // required dependencies
            if (p.softdepend() != null && considerSoftDependencies)
                deps.addAll(p.softdepend());
        }

        // Get the difference between the set of manually installed plugins,
        // the set of required dependencies, and the set of all installed plugins.
        return installed.values().stream()
            .filter(it -> !manual.contains(it.name()) && !deps.contains(it.name())).toList();
    }
}
