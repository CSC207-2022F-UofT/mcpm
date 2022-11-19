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

/**
 * Controller class for the ListAll use case.
 *
 * @author Kevin Chen
*/
public class ListAllController {
    ListAllBoundary listAllBoundary;

    /**
     * Constructor for ListAllController.
     *
     * @param listAllBoundary The boundary class for ListAllController.
     */
    public ListAllController(ListAllBoundary listAllBoundary) {
        this.listAllBoundary = listAllBoundary;
    }

    /**
     * Executes the ListAll use case.
     *
     * @param parameter The parameter for the ListAll use case.
     * @return The list of plugins.
     */
    List<String> listAll(String parameter) {

        String[] validIn = {"all", "manual", "outdated"};
        try {
            if (Arrays.asList(validIn).contains(parameter)) {
                return listAllBoundary.listBoundary(parameter);
            } else {
                throw new IllegalArgumentException("Invalid parameter");
            }
        } catch (Exception e) { 
            e.printStackTrace();
            return null;
        }
    }
}
