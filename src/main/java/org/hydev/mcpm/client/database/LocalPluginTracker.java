package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.HashUtils;
import org.hydev.mcpm.utils.PluginJarFile;

import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.*;  
import java.util.Scanner;

import javax.naming.NameNotFoundException;
import javax.swing.plaf.metal.MetalIconFactory.FileIcon16;  

/**
 * This class keeps track of locally installed packages
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @author Kevin (https://github.com/kchprog)
 * @since 2022-09-27
 */

public class LocalPluginTracker implements PluginTracker 
{
    // CSV file storing the list of manually installed plugins
    private String mainLockFile = "TODO: Get this path";

    // Directory containing the plugins
    private String pluginDirectory = "TODO: Get this path";

    // Constructor 
    public LocalPluginTracker(String mainLockFileUrl, String pluginDirectoryUrl) 
    {
        this.mainLockFile = mainLockFileUrl;
        this.pluginDirectory = pluginDirectoryUrl;
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
        } catch (Exception e) {
            System.out.printf("Error reading plugin.yml from " + jar);
            return null;
        }
    }

    /**
     * List all currently installed plugins in an ArrayList
     */
    public List<PluginYml> listInstalled()
    {   
        List<PluginYml> installedPlugins = new ArrayList<>();
        // Go into the plugin directory and read the metadata of all the plugins
        File dir = new File(pluginDirectory);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try {
                    installedPlugins.add(readMeta(child));
                } catch (Exception e) {
                    System.out.printf("Error reading plugin.yml from " + child);
                }
            }
        } else {
            System.out.printf("Error reading directory");
        }

        return installedPlugins;
    }

    /**
     * Update CSV by row and column, helper function
     *
     * @param replace Replacement for your cell value
     * @param row Row for which need to update 
     * @param col Column for which you need to update
     * @throws IOException An IOException is thrown when there is an issue reading from the main lock file.
     */
    private void updateCsv(String replace, int row, int col) throws IOException {

        try {
            File inputFile = new File(mainLockFile);

            // Read existing file 
            CSVReader reader = new CSVReader(new FileReader(inputFile));
            List<String[]> csvBody = reader.readAll();
            // get CSV row column  and replace with by using row and column
            csvBody.get(row)[col] = replace;
            reader.close();

            // Write to CSV file which is open
            CSVWriter writer = new CSVWriter(new FileWriter(inputFile));
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.printf("Error updating CSV");
        }
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
    
    public void addManuallyInstalled(String name)
    {
        // Locate the name in the list of installed plugins and set the value in the second row as true

        // Read the CSV file and find the row with the plugin name.
        // Then, update the second column to true by calling the updateCsv function
        // If the plugin is not found, throw an error

        for (int i = 0; i < listInstalled().size(); i++) {
            if (listInstalled().get(i).name().equals(name)) {
                try {
                    updateCsv("true", i, 1);
                } catch (IOException e) {
                    System.out.printf("Error updating CSV");
                }
            }
        }

        throw new IllegalArgumentException("Plugin not found, verify whether installed.");

    }

    /**
     * Remove a plugin from the manually installed plugin list
     *
     * @param name Plugin name
     */
    public void removeManuallyInstalled(String name) {
        for (int i = 0; i < listInstalled().size(); i++) {
            if (listInstalled().get(i).name().equals(name)) {
                try {
                    updateCsv("false", i, 1);
                } catch (IOException e) {
                    System.out.printf("Error updating CSV");
                }
            }
        }
        throw new IllegalArgumentException("Plugin not found, verify whether installed.");
    }
    
    /**
     * Get a list of manually installed plugins
     *
     * @return List of plugin names
     */
    public List<String> listManuallyInstalled()
    {
        try {
            FileReader filereader = new FileReader(mainLockFile);


            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            List<String> manuallyInstalledPlugins = new ArrayList<>();

            // Read data 
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord[1].equals("true")) {
                    manuallyInstalledPlugins.add(nextRecord[0]);
                }
            }

            csvReader.close();
            return manuallyInstalledPlugins;
        } catch (Exception e) {
            System.out.printf("Error reading CSV");
            return null;
        }
    }

    /**
     * Get a list of automatically installed plugin dependencies that are no longer required
     *
     * @return List of plugin names
     */
    public List<String> listOrphanPlugins(boolean considerSoftDependencies)
    {
        
        List<String> orphanPlugins = new ArrayList<String>();
        List<String> manuallyinstalledPlugins = listManuallyInstalled();
        List<String> requiredDependencies = new ArrayList<String>();

        // Get all the dependencies of the manually installed plugins
        for (String name : manuallyinstalledPlugins) {
            try {
                // Find the pluginYml file of the plugin with name name from the plugin directory
                String pluginYmlPath = pluginDirectory + "/" + name + "/plugin.yml";
                PluginYml currPlugin = readMeta(new File(pluginYmlPath));
                // Add the dependencies of the plugin to the list of required dependencies
                requiredDependencies.addAll(currPlugin.depend());

                // If considerSoftDependencies is true, add the soft dependencies to the list of required dependencies
                if (considerSoftDependencies) {
                    requiredDependencies.addAll(currPlugin.softdepend());
                }

            } catch (Exception e) {
                System.out.printf("Error getting dependencies of " + name);
            }
        }

        // Get the difference between the set of manually installed plugins,
        // the set of required dependencies, and the set of all installed plugins.
        List<PluginYml> installedPluginsYml = listInstalled();

        // Create a list of all installed plugin names in string format from the installedPluginsYML list
        List<String> installedPlugins = new ArrayList<String>();
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
     *Return the version data of a plugin from the pluginYML file
     *
     * @param name Plugin id
     * @return Version data
     */

    public String getVersion(String name) {
        // Locate the file in the plugin directory and read the version from the plugin.yml
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
                            System.out.printf("Error reading plugin.yml version from " + child);
                        }
                    }
                }
            } else {
                System.out.printf("Plugin with id " + name + " not found");
                return "";
            }
        } catch (Exception e) {
            System.out.printf("Error reading plugin.yml from " + name);
        } 
        return "";
    }

    /**
     * Compares the hash of the locally-installed plugin of a specified version with the hash of the plugin of that version on the server
     *
     * @return True if the hashes match, false otherwise
     */
    /*
    public boolean compareHash(File local, String remote) {
        // Get the hash of the plugin with name name and version version from the server
        // Get the hash of the plugin with name name and version version from the local plugin directory
        // Compare the two hashes and return the result

        try {
            HashUtils hashUtils = new HashUtils();
            String localHash = hashUtils.hash(local);

            

            // Download the plugin from the server into a temporary directory, get its hash, and delete it
            // String remoteHash = hashUtils.hash(new File("temp/" + name + "-" + version + ".jar"));
            String remoteHash = remote;

            return localHash.equals(remoteHash);
        } catch (Exception e) {
            System.out.printf("Error getting hash");
            return false;
        }
    }
    */ 
}
