package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.HashUtils;
import org.hydev.mcpm.utils.PluginJarFile;
import org.hydev.mcpm.client.database.inputs.*;

import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.*;  
import java.util.Scanner;
import java.util.Arrays;

import javax.naming.NameNotFoundException;
import javax.swing.plaf.metal.MetalIconFactory.FileIcon16;  

import java.util.List;

public class ListAllController {
    ListAllBoundary listAllBoundary;

    public ListAllController(ListAllBoundary listAllBoundary) {
        this.listAllBoundary = listAllBoundary;
    }

    List<String> listAll(String parameter) {

        String[] validIn = {"all", "manual", "outdated"};

        if (Arrays.asList(validIn).contains(parameter)) {
            return listAllBoundary.listBoundary(parameter);
        } else {
            throw new IllegalArgumentException("Invalid parameter");
        }
    }
}
