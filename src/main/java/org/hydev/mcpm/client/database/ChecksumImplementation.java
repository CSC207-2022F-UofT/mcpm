package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.PluginJarFile;

import com.opencsv.CSVWriter;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.*;  
import java.util.Scanner;
import java.util.jar.JarFile;

import javax.naming.NameNotFoundException;
import javax.swing.plaf.metal.MetalIconFactory.FileIcon16; 

public class ChecksumImplementation {
    // Default path for the github database where things are hosted
    private String onlineDatabasePath = "TODO: Get this path";
    private String localDatabasePath = "TODO: Get this path"; // Default path for local database

    // Constructor
    public ChecksumImplementation(String onlineDatabasePath, String localDatabasePath) {
        this.onlineDatabasePath = onlineDatabasePath;
        this.localDatabasePath = localDatabasePath;
    }

    
}
