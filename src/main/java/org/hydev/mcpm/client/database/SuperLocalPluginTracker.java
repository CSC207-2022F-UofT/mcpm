package org.hydev.mcpm.client.database;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.tuple.Pair;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.PluginJarFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * Representation of a plugin for use in the local plugin tracker.
 */

class PluginTrackerModel {
    String name;
    Boolean isManual;
    String versionId;
    String pluginId;

    public PluginTrackerModel(String name, Boolean isManual, String versionId, String pluginId) {
        this.name = name;
        this.isManual = isManual;
        this.versionId = versionId;
        this.pluginId = pluginId;
    }

    public PluginTrackerModel(String stringRepresentation) {
        String[] split = stringRepresentation.split(",");
        this.name = split[0];
        this.isManual = Boolean.parseBoolean(split[1]);
        this.versionId = split[2];
        this.pluginId = split[3];
    }

    public String getName() {
        return name;
    }

    public Boolean getManual() {
        return isManual;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getPluginId() {
        return pluginId;
    }
}

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
    private String mainLockFile = "plugins/mcpm.lock.csv";

    // Directory containing the plugins
    private String pluginDirectory = "plugins";

    final ObjectMapper mapper = new ObjectMapper();

    /*
     * Instantiates a LocalPluginTracker with default parameters for general use
     */
    public SuperLocalPluginTracker() {
    }

    /**
     * Instantiates a LocalPluginTracker with custom parameters for testing or
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
            System.out.println("Error reading plugin.yml from " + jar);
            return null;
        }
    }

    /**
     * Read the CSV file and return a mapping between plugin names and install
     * status
     *
     * @return Mapping between plugin name and boolean status
     */
    private ArrayList<PluginTrackerModel> readCsv() {
        ArrayList<PluginTrackerModel> accumulator = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(mainLockFile));
            String[] line;
            while ((line = reader.readNext()) != null) {
                accumulator.add(new PluginTrackerModel(line[0], Boolean.parseBoolean(line[1]), line[2], line[3]));
            }
            reader.close();
            return accumulator;
        } catch (FileNotFoundException e) {
            return accumulator;
        } catch (CsvException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read a JSON file at the given location file and return a mapping between
     * plugin names and install
     * status
     *
     * @param csv The mapping between plugin name and boolean status
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

    /**
     * Save an ArrayList's contents, overwriting a CSV file
     *
     * @param list ArrayList of PluginTrackerModel instances to save
     *
     */
    private void saveCsv(ArrayList<PluginTrackerModel> listToSave) {
        String csvFile = mainLockFile;
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            for (PluginTrackerModel pluginTrackerModel : listToSave) {
                String[] data = { pluginTrackerModel.name, pluginTrackerModel.isManual.toString(),
                        pluginTrackerModel.versionId, pluginTrackerModel.pluginId };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add a plugin to the CSV file
     *
     * @param name   Plugin name
     * @param status Plugin status (true = manual, false = auto)
     */
    public void addEntry(String name, boolean status, String versionId, String pluginId) {
        var currentList = readCsv();
        currentList.add(new PluginTrackerModel(name, status, versionId, pluginId));
        saveCsv(currentList);
    }

    /**
     * Remove a plugin with the given name from the CSV file
     *
     * @param name Plugin name
     */
    public void removeEntry(String name) {
        var currentList = readCsv();
        currentList.removeIf(pluginTrackerModel -> pluginTrackerModel.name.equals(name));
        saveCsv(currentList);
    }

    /**
     * Synchronize locally installed plugins at pluginDirectory with the CSV file
     * (add new plugins, remove deleted plugins)
     * 
     * @param tryPreserveLocalStatus If true, try to preserve the local status of
     *                               the plugin (manual)
     */
    public void syncMainLockFile(Boolean tryPreserveLocalStatus) {
        ArrayList<PluginTrackerModel> currentList = readCsv();

        HashMap<String, PluginTrackerModel> currentListName = new HashMap<>();

        for (PluginTrackerModel pluginTrackerModel : currentList) {
            currentListName.put(pluginTrackerModel.name, pluginTrackerModel);
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
            PluginTrackerModel PluginRepresentation = new PluginTrackerModel(pluginYml.name(), false,
                    pluginYml.version(), "unknown");

            // if the plugin exists in currentList and installedPlugins,
            // add it to toAdd with its currently stored parameters and status

            if (currentListName.containsKey(PluginRepresentation.name)) {
                if (currentListName.get(PluginRepresentation.name).versionId.equals(PluginRepresentation.versionId)) {
                    // if the plugin exists in both currentList and installedPlugins, pass it on
                    toAdd.add(currentListName.get(PluginRepresentation.name));
                } else if (tryPreserveLocalStatus) {
                    // if the plugin exists in both currentList and installedPlugins, but the
                    // version differs
                    // and we are trying to preserve local status, pass it on with the same status
                    PluginRepresentation.isManual = currentListName.get(PluginRepresentation.name).isManual;
                }
                // Overwrite the local representation with a new one
                toAdd.add(PluginRepresentation);

            } else {
                toAdd.add(PluginRepresentation);
            }
        }

        saveCsv(toAdd);
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
     * Precondition: mainLockFile is a sorted .csv file with the following format:
     * 1st column: Plugin name
     * 2nd column: Manually added (true/false)
     * Example:
     * PluginName,true
     * PluginName2,false
     *
     * @param name Plugin name
     */
    public void setManuallyInstalled(String name) {
        ArrayList<PluginTrackerModel> currentList = readCsv();

        ArrayList<PluginTrackerModel> newList = new ArrayList<>();

        for (PluginTrackerModel pluginTrackerModel : currentList) {
            if (pluginTrackerModel.name.equals(name)) {
                pluginTrackerModel.isManual = true;
            }
            newList.add(pluginTrackerModel);
        }

        saveCsv(newList);
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
        ArrayList<PluginTrackerModel> currentList = readCsv();

        ArrayList<PluginTrackerModel> newList = new ArrayList<>();

        for (PluginTrackerModel pluginTrackerModel : currentList) {
            if (pluginTrackerModel.name.equals(name)) {
                pluginTrackerModel.isManual = false;
            }
            newList.add(pluginTrackerModel);
        }

        saveCsv(newList);
    }

    /**
     * Get a list of manually installed plugins
     *
     * @return List of plugin names
     */
    public List<String> listManuallyInstalled() {
        ArrayList<PluginTrackerModel> currentList = readCsv();

        ArrayList<String> newList = new ArrayList<>();

        for (PluginTrackerModel pluginTrackerModel : currentList) {
            if (pluginTrackerModel.isManual) {
                newList.add(pluginTrackerModel.name);
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

            } catch (Exception e) {
                System.out.println("Error getting dependencies of " + name);
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
        } catch (Exception e) {
            System.out.println("Error reading plugin.yml from " + name);
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
            } catch (Exception e) {
                System.out.println("Error checking if plugin " + plugin.name() + " is outdated");
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
                System.out.println("Error getting hash from server");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error fetching local plugin version");
            return false;
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
            System.out.println("Error comparing versions");
            return false;
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
        try {
            // Find the file from the plugin directory
            File dir = new File(pluginDirectory);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if (child.getName().equals(name)) {
                        return child;
                    }
                }
                throw new IllegalArgumentException("Plugin not found, verify whether installed.");
            } else {
                throw new Exception("Empty Directory.");
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Plugin not found, error with directory iteration.");
        }
    }

    /**
     * Tests. TODO: Move this to tests
     *
     * @param args Arguments (not used)
     */
    public static void main(String[] args) {
        LocalPluginTracker myLocalPluginTracker = new LocalPluginTracker();

        // Test listInstalled
        List<PluginYml> installedPlugins = myLocalPluginTracker.listInstalled();
        for (PluginYml plugin : installedPlugins) {
            System.out.println(plugin.name());
        }

        // Test listOutdated

    }
}
