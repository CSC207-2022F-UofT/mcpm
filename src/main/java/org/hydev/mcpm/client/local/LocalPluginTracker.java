//package org.hydev.mcpm.client.local;
//
//import com.opencsv.CSVReader;
//import com.opencsv.CSVWriter;
//import com.opencsv.exceptions.CsvException;
//import org.apache.commons.lang3.tuple.Pair;
//import org.hydev.mcpm.client.database.tracker.PluginTracker;
//import org.hydev.mcpm.client.search.SearchPackagesBoundary;
//import org.hydev.mcpm.client.search.SearchPackagesInput;
//import org.hydev.mcpm.client.search.SearchPackagesType;
//import org.hydev.mcpm.client.search.SearchPackagesResult;
//import org.hydev.mcpm.client.models.PluginModel;
//import org.hydev.mcpm.client.models.PluginVersion;
//import org.hydev.mcpm.client.models.PluginYml;
//import org.hydev.mcpm.utils.PluginJarFile;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * This class keeps track of locally installed packages
// */
//public class LocalPluginTracker implements PluginTracker {
//    // CSV file storing the list of manually installed plugins
//    private String mainLockFile = "plugins/mcpm.lock.csv";
//
//    // Directory containing the plugins
//    private String pluginDirectory = "plugins";
//
//    /*
//     * Instantiates a LocalPluginTracker with default parameters for general use
//     */
//    public LocalPluginTracker() {
//    }
//
//    /**
//     * Instantiates a LocalPluginTracker with custom parameters for testing or
//     *
//     * @param mainLockFile    The path to the main lock file
//     * @param pluginDirectory The path to the plugin directory
//     */
//    public LocalPluginTracker(String mainLockFile, String pluginDirectory) {
//        this.mainLockFile = mainLockFile;
//        this.pluginDirectory = pluginDirectory;
//    }
//
//    /**
//     * Read metadata from a plugin's jar
//     *
//     * @param jar Local plugin jar path
//     * @return Metadata
//     */
//    private PluginYml readMeta(File jar) {
//        try (PluginJarFile InstancePluginJarFile = new PluginJarFile(jar)) {
//            return InstancePluginJarFile.readPluginYaml();
//        } catch (Exception e) {
//            System.out.println("Error reading plugin.yml from " + jar);
//            return null;
//        }
//    }
//
//    /**
//     * Read the CSV file and return a mapping between plugin names and install
//     * status
//     *
//     * @return Mapping between plugin name and boolean status
//     */
//    private Map<String, Boolean> readCsv() {
//        Map<String, Boolean> map = new HashMap<>();
//        try {
//            CSVReader reader = new CSVReader(new FileReader(mainLockFile));
//            String[] line;
//            while ((line = reader.readNext()) != null) {
//                map.put(line[0], Boolean.parseBoolean(line[1]));
//            }
//            reader.close();
//            return map;
//        } catch (FileNotFoundException e) {
//            return map;
//        } catch (CsvException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Save a hashmap's contents, overwriting a CSV file
//     *
//     * @param map Hashmap to save
//     *
//     */
//    private void saveCsv(Map<String, Boolean> map) {
//        String csvFile = mainLockFile;
//        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
//            for (Map.Entry<String, Boolean> entry : map.entrySet()) {
//                String[] line = { entry.getKey(), entry.getValue().toString() };
//                writer.writeNext(line);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Add a plugin to the CSV file
//     *
//     * @param name   Plugin name
//     * @param status Plugin status (true = manual, false = auto)
//     */
//    public void addEntry(String name, boolean status) {
//        Map<String, Boolean> map = readCsv();
//        map.put(name, status);
//        saveCsv(map);
//    }
//
//    /**
//     * Remove a plugin with the given name from the CSV file
//     *
//     * @param name Plugin name
//     */
//    public void removeEntry(String name) {
//        Map<String, Boolean> map = readCsv();
//        map.remove(name);
//        saveCsv(map);
//    }
//
//    /**
//     * Synchronize locally installed plugins at pluginDirectory with the CSV file
//     */
//    public void syncMainLockFile() {
//        Map<String, Boolean> csvMap = readCsv();
//
//        Set<String> installedMap = new HashSet<>();
//
//        List<PluginYml> installedPlugins = listInstalled();
//
//        for (PluginYml plugin : installedPlugins) {
//            installedMap.add(plugin.name());
//        }
//
//        ArrayList<Pair<String, Boolean>> toAdd = new ArrayList<>();
//
//        // If a key value pair exists in installedMap but not in the csv map, add its
//        // representation to toAdd.
//        // If a kvp exists in both, add it with toAdd, keeping the value from the csv
//        // map.
//        for (String entry : installedMap) {
//            toAdd.add(Pair.of(entry, csvMap.getOrDefault(entry, false)));
//        }
//
//        // Return a new map containing all of temp's elements, turned into a map.
//        Map<String, Boolean> newMap = toAdd.stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
//
//        // Save the new map to the csv file.
//        saveCsv(newMap);
//    }
//
//    /**
//     * List all currently installed plugins in an ArrayList
//     */
//    public List<PluginYml> listInstalled() {
//        // Go into the plugin directory and list files
//        File dir = new File(pluginDirectory);
//        File[] list = dir.listFiles();
//        if (list == null)
//            return new ArrayList<>();
//
//        // Filter only java files, return all metadata that's not null
//        return Arrays.stream(list).filter(f -> f.isFile() && f.getName().endsWith(".jar"))
//                .map(this::readMeta).filter(Objects::nonNull).toList();
//    }
//
//    /**
//     * Mark a plugin as manually installed (as opposed to a dependency)
//     * Precondition: mainLockFile is a sorted .csv file with the following format:
//     * 1st column: Plugin name
//     * 2nd column: Manually added (true/false)
//     * Example:
//     * PluginName,true
//     * PluginName2,false
//     *
//     * @param name Plugin name
//     */
//    public void setManuallyInstalled(String name) {
//        Map<String, Boolean> mainLock = readCsv();
//
//        if (mainLock.containsKey(name)) {
//            mainLock.replace(name, true);
//            saveCsv(mainLock);
//        }
//    }
//
//    /**
//     * Remove a plugin from the manually installed plugin list
//     *
//     * @param name Plugin name
//     */
//    public void removeManuallyInstalled(String name) {
//        // Locate the name in the list of installed plugins and set the value in the
//        // second row as false
//        // Load in the csv file
//        Map<String, Boolean> mainLock = readCsv();
//
//        if (mainLock.containsKey(name)) {
//            mainLock.replace(name, false);
//            saveCsv(mainLock);
//        }
//    }
//
//    /**
//     * Get a list of manually installed plugins
//     *
//     * @return List of plugin names
//     */
//    public List<String> listManuallyInstalled() {
//        Map<String, Boolean> mainLock = readCsv();
//
//        ArrayList<String> accumulator = new ArrayList<>();
//
//        for (Map.Entry<String, Boolean> entry : mainLock.entrySet()) {
//            if (entry.getValue()) {
//                accumulator.add(entry.getKey());
//            }
//        }
//
//        // Sorts the list alphabetically
//        Collections.sort(accumulator);
//
//        return accumulator;
//    }
//
//    /**
//     * Get a list of automatically installed plugin dependencies that are no longer
//     * required
//     *
//     * @return List of plugin names
//     */
//    public List<String> listOrphanPlugins(boolean considerSoftDependencies) {
//
//        List<String> manuallyInstalledPlugins = listManuallyInstalled();
//        List<String> requiredDependencies = new ArrayList<>();
//
//        // Get all the dependencies of the manually installed plugins
//        for (String name : manuallyInstalledPlugins) {
//            try {
//                // Find the pluginYml file of the plugin with name from the plugin
//                // directory
//
//                File pluginYmlPath = getPluginFile(name);
//                PluginYml p = readMeta(pluginYmlPath);
//                if (p == null)
//                    continue;
//
//                // String pluginYmlPath = pluginDirectory + "/" + name + "/plugin.yml";
//                // PluginYml currPlugin = readMeta(new File(pluginYmlPath));
//                // Add the dependencies of the plugin to the list of required dependencies
//                if (p.depend() != null)
//                    requiredDependencies.addAll(p.depend());
//
//                // If considerSoftDependencies is true, add the soft dependencies to the list of
//                // required dependencies
//                if (considerSoftDependencies) {
//                    if (p.softdepend() != null)
//                        requiredDependencies.addAll(p.softdepend());
//                }
//
//            } catch (Exception e) {
//                System.out.println("Error getting dependencies of " + name);
//            }
//        }
//
//        // Get the difference between the set of manually installed plugins,
//        // the set of required dependencies, and the set of all installed plugins.
//        List<PluginYml> installedPluginsYml = listInstalled();
//
//        // Create a list of all installed plugin names in string format from the
//        // installedPluginsYML list
//        List<String> installedPlugins = new ArrayList<>();
//        for (PluginYml plugin : installedPluginsYml) {
//            installedPlugins.add(plugin.name());
//        }
//
//        // Get the difference between the set of manually installed plugins,
//        // the set of required dependencies, and the set of all installed plugins
//        installedPlugins.removeAll(requiredDependencies);
//        installedPlugins.removeAll(manuallyInstalledPlugins);
//
//        return installedPlugins;
//    }
//
//    /**
//     * Return the version data of a plugin from the pluginYML file
//     *
//     * @param name Plugin id
//     * @return Version data
//     */
//
//    public String getVersion(String name) {
//        // Locate the file in the plugin directory and read the version from the
//        // plugin.yml
//        // If the plugin is not found, throw an error
//
//        try {
//            File dir = new File(pluginDirectory);
//            File[] directoryListing = dir.listFiles();
//            if (directoryListing != null) {
//                for (File child : directoryListing) {
//                    if (child.getName().equals(name)) {
//                        try {
//                            var meta = readMeta(child);
//
//                            if (meta != null) {
//                                return meta.version();
//                            }
//
//                            return null;
//                        } catch (Exception e) {
//                            System.out.println("Error reading plugin.yml version from " + child);
//                        }
//                    }
//                }
//            } else {
//                System.out.println("Plugin with id " + name + " not found");
//                return "";
//            }
//        } catch (Exception e) {
//            System.out.println("Error reading plugin.yml from " + name);
//        }
//        return "";
//    }
//
//    /// **
//    // * Get a list of plugins that are outdated
//    // *
//    // * @return List of plugin names
//    // */
//    /*
//     * public List<String> listOutdated() {
//     * List<String> outdatedPlugins = new ArrayList<String>();
//     * List<PluginYml> installedPlugins = listInstalled();
//     *
//     * // For each plugin in the list of installed plugins, check if the version in
//     * the plugin.yml file is outdated
//     * // If it is, add the plugin name to the list of outdated plugins
//     * for (PluginYml plugin : installedPlugins) {
//     * try {
//     * if (compareVersion(mainLockFile)) {
//     * outdatedPlugins.add(plugin.name());
//     * }
//     * } catch (Exception e) {
//     * System.out.println("Error checking if plugin " + plugin.name() +
//     * " is outdated");
//     * }
//     * }
//     *
//     * return outdatedPlugins;
//     * }
//     */
//
//    /**
//     * Get a list of plugin (as pluginYml) that are outdated
//     *
//     * @return List of plugin names
//     */
//    public List<PluginYml> listOutdatedPluginYml(SearchPackagesBoundary searchPackagesBoundary) {
//        List<PluginYml> outdatedPlugins = new ArrayList<>();
//        List<PluginYml> installedPlugins = listInstalled();
//
//        // For each plugin in the list of installed plugins, check if the version in the
//        // plugin.yml file is outdated
//        // If it is, add the plugin name to the list of outdated plugins
//        for (PluginYml plugin : installedPlugins) {
//            try {
//                if (compareVersion(plugin.name(), searchPackagesBoundary)) {
//                    outdatedPlugins.add(plugin);
//                }
//            } catch (Exception e) {
//                System.out.println("Error checking if plugin " + plugin.name() + " is outdated");
//            }
//        }
//
//        return outdatedPlugins;
//    }
//
//    /**
//     * Compare whether the locally installed version of the plugin matches the
//     * version on the server.
//     * If yes, return true. If no, return false.
//     *
//     * @return True if the local version of plugin with name is outdated, false
//     *         otherwise
//     */
//    public Boolean compareVersion(String name, SearchPackagesBoundary searchPackagesBoundary) {
//        try {
//            File pluginYmlPath = getPluginFile(name);
//            PluginYml currPlugin = readMeta(pluginYmlPath);
//
//            if (currPlugin == null) {
//                return false;
//            }
//
//            String localVersion = currPlugin.version();
//
//            SearchPackagesInput searchPackagesInput = new SearchPackagesInput(SearchPackagesType.BY_NAME, name,
//                    false);
//            SearchPackagesResult searchPackagesResult = searchPackagesBoundary.search(searchPackagesInput);
//
//            // Get the version of the plugin from the server: Query for all, see if there's
//            // a match
//            if (searchPackagesResult.state().equals(SearchPackagesResult.State.SUCCESS)) {
//                for (PluginModel plugin : searchPackagesResult.plugins()) {
//                    PluginVersion latest = plugin.getLatestPluginVersion().orElse(null);
//                    if (latest != null) {
//                        if (latest.meta().version().equals(localVersion)) {
//                            return true;
//                        }
//                    }
//
//                }
//            } else {
//                System.out.println("Error getting hash from server");
//                return false;
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error fetching local plugin version");
//            return false;
//        }
//        return false;
//    }
//
//    /**
//     * Get whether a local plugin File matches the version of the plugin on the
//     * server
//     *
//     * @param local  the local plugin File
//     * @param remote a PluginModel object representing the plugin on the server
//     * @return True if this plugin is up-to-date.
//     */
//    public Boolean compareVersionNew(File local, PluginModel remote) {
//        try {
//            PluginYml localPlugin = readMeta(local);
//
//            if (localPlugin == null) {
//                return false;
//            }
//
//            String localVersion = localPlugin.version();
//            String remoteVersion = remote.versions().get(0).meta().version();
//
//            return localVersion.equals(remoteVersion);
//        } catch (Exception e) {
//            System.out.println("Error comparing versions");
//            return false;
//        }
//    }
//
//    /**
//     * Retrieves the file path of a plugin with a specified name as a File
//     *
//     * @param name Plugin name
//     * @return A File object representation of the plugin
//     */
//    private File getPluginFile(String name) {
//        // Get the file path of the plugin with name from the local plugin
//        // directory
//        // Return the file path as a File
//        try {
//            // Find the file from the plugin directory
//            File dir = new File(pluginDirectory);
//            File[] directoryListing = dir.listFiles();
//            if (directoryListing != null) {
//                for (File child : directoryListing) {
//                    if (child.getName().equals(name)) {
//                        return child;
//                    }
//                }
//                throw new IllegalArgumentException("Plugin not found, verify whether installed.");
//            } else {
//                throw new Exception("Empty Directory.");
//            }
//
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Plugin not found, error with directory iteration.");
//        }
//    }
//
//    /**
//     * Tests. TODO: Move this to tests
//     *
//     * @param args Arguments (not used)
//     */
//    public static void main(String[] args) {
//        LocalPluginTracker myLocalPluginTracker = new LocalPluginTracker();
//
//        // Test listInstalled
//        List<PluginYml> installedPlugins = myLocalPluginTracker.listInstalled();
//        for (PluginYml plugin : installedPlugins) {
//            System.out.println(plugin.name());
//        }
//
//        // Test listOutdated
//
//    }
//}
