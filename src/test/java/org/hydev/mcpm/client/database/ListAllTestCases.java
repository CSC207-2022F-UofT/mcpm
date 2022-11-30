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

import org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ListAllTestCases {
    final String pluginDirectory = "plugins";

    private ArrayList<PluginYml> listAllPluginsHelper() {
        ArrayList<PluginYml> plugins = new ArrayList<>();

        try (PluginJarFile InstancePluginJarFile = new PluginJarFile(new File(pluginDirectory))) {
            for (File file : Objects.requireNonNull(new File(pluginDirectory).listFiles())) {
                if (file.getName().endsWith(".jar")) {
                    PluginYml pluginYml = InstancePluginJarFile.readPluginYaml();
                    if (pluginYml != null) {
                        plugins.add(pluginYml);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading plugin.yml from " + pluginDirectory);
        }
        return plugins;

    }

    @Test
    public void testListAll() {
        LocalPluginTracker localPluginTracker = new LocalPluginTracker();
        var installed = localPluginTracker.listInstalled();

        var allInstalled = listAllPluginsHelper();
        assertEquals(installed, allInstalled);
    }
}
