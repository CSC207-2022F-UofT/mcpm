package org.hydev.mcpm.client.database;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.client.models.PluginYml.InvalidPluginMetaStructure;
import org.hydev.mcpm.utils.PluginJarFile;
import org.hydev.mcpm.client.models.PluginTrackerModel;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class keeps track of locally installed packages
 *
 * @author Kevin (https://github.com/kchprog)
 * @since 2022-09-27
 */
public class SuperLocalPluginTracker implements SuperPluginTracker {
    // CSV file storing the list of manually installed plugins.
    // Now, each row in the csv file represents something as follows:
    // ""name", "boolean", "versionId", "pluginId"

    private String mainLockFile = "plugins/mcpm.lock.json";

    // Directory containing the plugins
    private String pluginDirectory = "plugins";

    final ObjectMapper mapper = new ObjectMapper();

    /**
     * Instantiates a LocalPluginTracker with default parameters for general use
     */
    public SuperLocalPluginTracker() {
    }

    /**
     * Instantiates a LocalPluginTracker with custom parameters for testing or other
     * purposes
     *
     * @param mainLockFile    The path to the main lock file
     * @param pluginDirectory The path to the plugin directory
     */
    public SuperLocalPluginTracker(String mainLockFile, String pluginDirectory) {
        this.mainLockFile = mainLockFile;
        this.pluginDirectory = pluginDirectory;
    }

    /**
     * Read metadata from a plugin's jar
     *
     * @param jar Local plugin jar path
     * @return Metadata
     */
    private PluginYml readMeta(File jar) {
        try (PluginJarFile InstancePluginJarFile = new PluginJarFile(jar)) {
            return InstancePluginJarFile.readPluginYaml();
        } catch (IOException e) {
            throw new RuntimeException("Error reading plugin.yml from " + jar.getAbsolutePath());
        } catch (InvalidPluginMetaStructure e) {
            throw new RuntimeException("Invalid plugin.yml structure in " + jar.getAbsolutePath());
        }
    }

    /*
     * Read the CSV file and return a mapping between plugin names and install
     * status
     *
     * @return Mapping between plugin name and boolean status
     */
    /*
     * private ArrayList<PluginTrackerModel> readCsv() {
     * ArrayList<PluginTrackerModel> accumulator = new ArrayList<>();
     * try {
     * CSVReader reader = new CSVReader(new FileReader(mainLockFile));
     * String[] line;
     * while ((line = reader.readNext()) != null) {
     * accumulator.add(new PluginTrackerModel(line[0],
     * Boolean.parseBoolean(line[1]), line[2], line[3]));
     * }
     * reader.close();
     * return accumulator;
     * } catch (FileNotFoundException e) {
     * return accumulator;
     * } catch (CsvException | IOException e) {
     * throw new RuntimeException(e);
     * }
     * }
     */

    /**
     * Read a JSON file at the given location file and return a mapping between
     * plugin names and install
     * status
     *
     */
    private ArrayList<PluginTrackerModel> readJson() {
        try {
            // ObjectMapper mapper = new ObjectMapper();
            // Reads the json file and converts it to a list of PluginTrackerModel objects
            return this.mapper.readValue(new File(mainLockFile), mapper.getTypeFactory()
                    .constructCollectionType(ArrayList.class, PluginTrackerModel.class));
        } catch (FileNotFoundException e) {
            return (new ArrayList<PluginTrackerModel>());
        } catch (JsonParseException | JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Save an ArrayList's contents, overwriting a CSV file
     *
     * @param list ArrayList of PluginTrackerModel instances to save
     *
     */
    /*
     * private void saveCsv(ArrayList<PluginTrackerModel> listToSave) {
     * String csvFile = mainLockFile;
     * try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
     * for (PluginTrackerModel pluginTrackerModel : listToSave) {
     * String[] data = { pluginTrackerModel.getName(),
     * pluginTrackerModel.isManual().toString(),
     * pluginTrackerModel.getVersionId(), pluginTrackerModel.getPluginId() };
     * writer.writeNext(data);
     * }
     * } catch (IOException e) {
     * throw new RuntimeException(e);
     * }
     * }
     */

    /**
     * Returns whether a plugin with the given name is within
     * the lock file
     *
     * @param name Id of the plugin
     * @return Whether the plugin is within the lock file
     */
    public Boolean findIfInLockById(String name) {
        ArrayList<PluginTrackerModel> list = readJson();
        for (PluginTrackerModel pluginTrackerModel : list) {
            if (pluginTrackerModel.getPluginId().equals(name)) {
                return true;
            }
        }
        return false;
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
     * @param name   Plugin name
     * @param status Plugin status (true = manual, false = auto)
     */
    public void addEntry(String name, boolean status, String versionId, String pluginId) {
        ArrayList<PluginTrackerModel> currentList = this.readJson();
        currentList.add(new PluginTrackerModel(name, status, versionId, pluginId));
        saveJson(currentList);
    }

    /**
     * Remove a plugin with the given name from the JSON file
     *
     * @param name Plugin name
     */

    public void removeEntry(String name) {
        ArrayList<PluginTrackerModel> currentList = this.readJson();
        currentList.removeIf(pluginTrackerModel -> pluginTrackerModel.getName().equals(name));
        saveJson(currentList);
    }

    /**
     * Add a plugin to the JSON file
     *
     * @param tryPreserveLocalStatus if true, attempt to preserve the local status
     *                               of the plugin using
     *                               the local status of the plugin with the same
     *                               name. This may be inaccurate
     *                               if multiple plugins share the same name
     * 
     */
    public void syncMainLockFile(Boolean tryPreserveLocalStatus) {
        ArrayList<PluginTrackerModel> currentList = readJson();

        HashMap<String, PluginTrackerModel> currentListName = new HashMap<>();

        for (PluginTrackerModel pluginTrackerModel : currentList) {
            currentListName.put(pluginTrackerModel.getName(), pluginTrackerModel);
        }

        List<PluginYml> installedPlugins = listInstalled();

        // Iterate through each plugin in installedPlugins. If the plugin exists in both
        // currentList and installedPlugins
        // add its representation in currentList to toAdd. If the plugin exists only in
        // installedPlugins, instantiate
        // a new PluginTrackerModel representing this newly-installed plugin, with
        // default manuallyInstalled false.

        ArrayList<PluginTrackerModel> toAdd = new ArrayList<>();

        for (PluginYml pluginYml : installedPlugins) {
            PluginTrackerModel pluginRepresentation = new PluginTrackerModel(pluginYml.name(), false,
                    pluginYml.version(), "unknown");

            // if the plugin exists in currentList and installedPlugins,
            // add it to toAdd with its currently stored parameters and status

            if (currentListName.containsKey(pluginRepresentation.getName())) {
                if (currentListName.getOrDefault(pluginRepresentation.getName(), null).equals(null)) {
                    // if the plugin exists in both currentList and installedPlugins, pass it on
                    toAdd.add(currentListName.get(pluginRepresentation.getName()));
                } else if (tryPreserveLocalStatus) {
                    // if the plugin exists in both currentList and installedPlugins, but the
                    // version differs
                    // and we are trying to preserve local status, pass it on with the same status
                    boolean status = currentListName.get(pluginRepresentation.getName()).isManual();
                    pluginRepresentation.setManual(status);
                }
                // Overwrite the local representation with a new one
                toAdd.add(pluginRepresentation);

            } else {
                toAdd.add(pluginRepresentation);
            }
        }

        saveJson(toAdd);
    }

    /**
     * List all currently installed plugins in an ArrayList
     */
    public List<PluginYml> listInstalled() {
        // Go into the plugin directory and list files
        File dir = new File(pluginDirectory);
        File[] list = dir.listFiles();
        if (list == null)
            return new ArrayList<>();

        // Filter only java files, return all metadata that's not null
        return Arrays.stream(list).filter(f -> f.isFile() && f.getName().endsWith(".jar"))
                .map(this::readMeta).filter(Objects::nonNull).toList();
    }

    /**
     * Mark a plugin as manually installed (as opposed to a dependency)
     * Precondition: mainLockFile is a JSON file containing a list of valid
     * PluginTrackerModel entries
     *
     * @param name Plugin name
     */
    public void setManuallyInstalled(String name) {
        ArrayList<PluginTrackerModel> currentList = readJson();
        ArrayList<PluginTrackerModel> newList = new ArrayList<>();

        for (PluginTrackerModel pluginTrackerModel : currentList) {
            if (pluginTrackerModel.getName().equals(name)) {
                pluginTrackerModel.setManual(true);
            }
            newList.add(pluginTrackerModel);
        }

        saveJson(newList);
    }

    /**
     * Remove a plugin from the manually installed plugin list
     *
     * @param name Plugin name
     */
    public void removeManuallyInstalled(String name) {
        // Locate the name in the list of installed plugins and set the value in the
        // second row as false
        // Load in the csv file
        ArrayList<PluginTrackerModel> currentList = readJson();

        ArrayList<PluginTrackerModel> newList = new ArrayList<>();

        for (PluginTrackerModel pluginTrackerModel : currentList) {
            if (pluginTrackerModel.getName().equals(name)) {
                pluginTrackerModel.setManual(false);
            }
            newList.add(pluginTrackerModel);
        }

        saveJson(newList);
    }

    /**
     * Get a list of manually installed plugins
     *
     * @return List of plugin names
     */
    public List<String> listManuallyInstalled() {
        ArrayList<PluginTrackerModel> currentList = readJson();

        ArrayList<String> newList = new ArrayList<>();

        for (PluginTrackerModel pluginTrackerModel : currentList) {
            if (pluginTrackerModel.isManual()) {
                newList.add(pluginTrackerModel.getName());
            }
        }
        return newList;
    }

    /**
     * Get a list of automatically installed plugin dependencies that are no longer
     * required
     *
     * @return List of plugin names
     */
    public List<String> listOrphanPlugins(boolean considerSoftDependencies) {

        List<String> orphanPlugins = new ArrayList<>();
        List<String> manuallyinstalledPlugins = listManuallyInstalled();
        List<String> requiredDependencies = new ArrayList<>();

        // Get all the dependencies of the manually installed plugins
        for (String name : manuallyinstalledPlugins) {
            try {
                // Find the pluginYml file of the plugin with name name from the plugin
                // directory

                File pluginYmlPath = getPluginFile(name);
                if (pluginYmlPath == null) {
                    throw new FileNotFoundException("Plugin " + name + " not found");
                }
                PluginYml p = readMeta(pluginYmlPath);
                if (p == null)
                    continue;

                // String pluginYmlPath = pluginDirectory + "/" + name + "/plugin.yml";
                // PluginYml currPlugin = readMeta(new File(pluginYmlPath));
                // Add the dependencies of the plugin to the list of required dependencies
                if (p.depend() != null)
                    requiredDependencies.addAll(p.depend());

                // If considerSoftDependencies is true, add the soft dependencies to the list of
                // required dependencies
                if (considerSoftDependencies) {
                    if (p.softdepend() != null)
                        requiredDependencies.addAll(p.softdepend());
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        // Get the difference between the set of manually installed plugins,
        // the set of required dependencies, and the set of all installed plugins.
        List<PluginYml> installedPluginsYml = listInstalled();

        // Create a list of all installed plugin names in string format from the
        // installedPluginsYML list
        List<String> installedPlugins = new ArrayList<>();
        for (PluginYml plugin : installedPluginsYml) {
            installedPlugins.add(plugin.name());
        }

        // Get the difference between the set of manually installed plugins,
        // the set of required dependencies, and the set of all installed plugins
        orphanPlugins = installedPlugins;
        orphanPlugins.removeAll(requiredDependencies);
        orphanPlugins.removeAll(manuallyinstalledPlugins);

        return orphanPlugins;
    }

    /**
     * Return the version data of a plugin from the pluginYML file
     *
     * @param name Plugin id
     * @return Version data
     */

    public String getVersion(String name) {
        // Locate the file in the plugin directory and read the version from the
        // plugin.yml
        // If the plugin is not found, throw an error

        try {
            File dir = new File(pluginDirectory);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if (child.getName().equals(name)) {
                        try {
                            return readMeta(child).version();
                        } catch (Exception e) {
                            System.out.println("Error reading plugin.yml version from " + child);
                        }
                    }
                }
            } else {
                System.out.println("Plugin with id " + name + " not found");
                return "";
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    /// **
    // * Get a list of plugins that are outdated
    // *
    // * @return List of plugin names
    // */
    /*
     * public List<String> listOutdated() {
     * List<String> outdatedPlugins = new ArrayList<String>();
     * List<PluginYml> installedPlugins = listInstalled();
     * 
     * // For each plugin in the list of installed plugins, check if the version in
     * the plugin.yml file is outdated
     * // If it is, add the plugin name to the list of outdated plugins
     * for (PluginYml plugin : installedPlugins) {
     * try {
     * if (compareVersion(mainLockFile)) {
     * outdatedPlugins.add(plugin.name());
     * }
     * } catch (Exception e) {
     * System.out.println("Error checking if plugin " + plugin.name() +
     * " is outdated");
     * }
     * }
     * 
     * return outdatedPlugins;
     * }
     */

    /**
     * Get a list of plugin (as pluginYml) that are outdated
     *
     * @return List of plugin names
     */
    public List<PluginYml> listOutdatedPluginYml(SearchPackagesBoundary searchPackagesBoundary) {
        List<PluginYml> outdatedPlugins = new ArrayList<PluginYml>();
        List<PluginYml> installedPlugins = listInstalled();

        // For each plugin in the list of installed plugins, check if the version in the
        // plugin.yml file is outdated
        // If it is, add the plugin name to the list of outdated plugins
        for (PluginYml plugin : installedPlugins) {
            try {
                if (compareVersion(plugin.name(), searchPackagesBoundary)) {
                    outdatedPlugins.add(plugin);
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }

        return outdatedPlugins;
    }

    /**
     * Compare whether the locally installed version of the plugin matches the
     * version on the server.
     * If yes, return true. If no, return false.
     *
     * @return True if the local version of plugin with name name is outdated, false
     *         otherwise
     */
    public Boolean compareVersion(String name, SearchPackagesBoundary searchPackagesBoundary) {
        try {
            File pluginYmlPath = getPluginFile(name);
            PluginYml currPlugin = readMeta(pluginYmlPath);
            String localVersion = currPlugin.version();

            SearchPackagesInput searchPackagesInput = new SearchPackagesInput(SearchPackagesType.BY_NAME, name,
                    false);
            SearchPackagesResult searchPackagesResult = searchPackagesBoundary.search(searchPackagesInput);

            // Get the version of the plugin from the server: Query for all, see if there's
            // a match
            if (searchPackagesResult.state().equals(SearchPackagesResult.State.SUCCESS)) {
                for (PluginModel plugin : searchPackagesResult.plugins()) {
                    PluginVersion latest = plugin.getLatestPluginVersion().orElse(null);
                    if (latest != null) {
                        if (latest.meta().version().equals(localVersion)) {
                            return true;
                        }
                    }

                }
            } else {
                return false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Get whether a local plugin File matches the version of the plugin on the
     * server
     *
     * @param local  the local plugin File
     * @param remote a PluginModel object representing the plugin on the server
     * @return List of plugin names
     */
    public Boolean compareVersionNew(File local, PluginModel remote) {
        try {
            PluginYml localPlugin = readMeta(local);
            String localVersion = localPlugin.version();
            String remoteVersion = remote.versions().get(0).meta().version();
            return localVersion.equals(remoteVersion);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the file path of a plugin with a specified name as a File
     *
     * @param name Plugin name
     * @return A File object representation of the plugin
     */
    private File getPluginFile(String name) {
        // Get the file path of the plugin with name name from the local plugin
        // directory
        // Return the file path as a File
        // Find the file from the plugin directory

        File dir = new File(pluginDirectory);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.getName().equals(name)) {
                    return child;
                }
            }
        } else {
            throw new IllegalArgumentException("Plugin not found, verify whether installed.");
        }
        return null;
    }

}
