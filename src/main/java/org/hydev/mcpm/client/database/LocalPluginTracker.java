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
public class LocalPluginTracker {
    // CSV file storing the list of manually installed plugins
    private static String MainLockFile = "TODO: Get this path";

    // Directory storing the plugins
    private static String PluginDirectory = "TODO: Get this path";

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
        // TODO: Implement this

        String tempfile = "temp.csv";
        File oldFile = new File(MainLockFile);
        File newFile = new File(tempfile);

        try {
            FileWriter fw = new FileWriter(tempfile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner scanner = new Scanner(new File(MainLockFile));
            scanner.useDelimiter("[,\n]");

            while (scanner.hasNext()) {
                String id = scanner.next();
                String state = scanner.next();
                if (id.equals(name)) {
                    pw.println(id + "," + "true");
                    return;
                }  else {
                    pw.println(id + "," + state);
                }
            }

            scanner.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(MainLockFile);
            newFile.renameTo(dump);

        } catch (Exception e) {
            System.out.printf("Error adding manually installed plugin, Unspecified");
        }

        throw new IllegalArgumentException("Plugin not found, verify whether installed.");

    }

    /**
     * Remove a plugin from the manually installed plugin list
     *
     * @param name Plugin name
     */
    public void removeManuallyInstalled(String name)
    {
        // TODO: Implement this
        String tempfile = "temp.csv";
        File oldFile = new File(MainLockFile);
        File newFile = new File(tempfile);

        try {
            FileWriter fw = new FileWriter(tempfile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner scanner = new Scanner(new File(MainLockFile));
            scanner.useDelimiter("[,\n]");

            while (scanner.hasNext()) {
                String id = scanner.next();
                String state = scanner.next();
                if (id.equals(name)) {
                    pw.println(id + "," + "false");
                    return;
                }  else {
                    pw.println(id + "," + state);
                }
            }

            scanner.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(MainLockFile);
            newFile.renameTo(dump);

        } catch (Exception e) {
            System.out.printf("Error adding manually installed plugin, Unspecified");
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


        try {
            FileWriter fw = new FileWriter(tempfile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner scanner = new Scanner(new File(MainLockFile));
            scanner.useDelimiter("[,\n]");

            while (scanner.hasNext() && scanner.next.hasNext()) {
                String id = scanner.next();
                String state = scanner.next();
                if (state.equals("true")) {
                    InstalledPlugins.add(id);
                }
            }

        } catch (Exception e) {
            System.out.printf("Error reading manually installed plugins, Unspecified.");
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
