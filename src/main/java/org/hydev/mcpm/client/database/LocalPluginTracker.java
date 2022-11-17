package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.PluginJarFile;

import com.opencsv.CSVWriter;

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
public class LocalPluginTracker implements PluginTracker {
    // CSV file storing the list of manually installed plugins
    private String MainLockFile = "TODO: Get this path";

    // Directory storing the plugins
    private String PluginDirectory = "TODO: Get this path";

    // Constructor 
    public LocalPluginTracker(String MainLockFile, String PluginDirectory) {
        this.MainLockFile = MainLockFile;
        this.PluginDirectory = PluginDirectory;
    }
    /**
     * Read metadata from a plugin's jar
     *
     * @param jar Local plugin jar path
     * @return Metadata
     */
    public PluginYml readMeta(File jar)
    {
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
        File dir = new File(PluginDirectory);
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
     * @throws IOException
     */
    public void updateCSV(String replace, int row, int col) throws IOException {

        File inputFile = new File(MainLockFile);

        // Read existing file 
        CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
        List<String[]> csvBody = reader.readAll();
        // get CSV row column  and replace with by using row and column
        csvBody.get(row)[col] = replace;
        reader.close();

        // Write to CSV file which is open
        CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',');
        writer.writeAll(csvBody);
        writer.flush();
        writer.close();
    }

    /**
     * Mark a plugin as manually installed (as opposed to a dependency)
     * Precondition: MainLockFile is a sorted .csv file with the following format:
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
        // Then, update the second column to true by calling the updateCSV function
        // If the plugin is not found, throw an error

        for (int i = 0; i < listInstalled().size(); i++) {
            if (listInstalled().get(i).getName().equals(name)) {
                try {
                    updateCSV("true", i, 1);
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
            if (listInstalled().get(i).getName().equals(name)) {
                try {
                    updateCSV("false", i, 1);
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
        List<String> InstalledPlugins = new ArrayList<String>();

        File inputFile = new File(MainLockFile);

        // Read existing file 
        CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
        List<String[]> csvBody = reader.readAll();

        // Iterate through each row and check if the second column is true
        // If it is, add the plugin name to the list
        for (int i = 0; i < csvBody.size(); i++) {
            if (csvBody.get(i)[1].equals("true")) {
                InstalledPlugins.add(csvBody.get(i)[0]);
            }
        }

        return InstalledPlugins;
    }

    /**
     * Get a list of automatically installed plugin dependencies that are no longer required
     *
     * @return List of plugin names
     */
    public List<String> listOrphans()
    {
        
        list<String> Orphans = new ArrayList<String>();
        List<String> ManuallyInstalledPlugins = listManuallyInstalled();
        List<String> RequiredDependencies = new ArrayList<String>();

        // Get all the dependencies of the manually installed plugins
        for (String plugin : ManuallyInstalledPlugins) {
            try {
                RequiredDependencies.addAll(getDependencies(plugin));
            } catch (Exception e) {
                System.out.printf("Error getting dependencies of " + plugin);
            }
        }

        // Get the difference between the set of manually installed plugins and the set of required dependencies, and the set of all installed plugins
        List<String> InstalledPlugins = listInstalled();
        Orphans = InstalledPlugins;
        Orphans.removeAll(RequiredDependencies);
        Orphans.removeAll(ManuallyInstalledPlugins);

        return Orphans;    
    }
}
