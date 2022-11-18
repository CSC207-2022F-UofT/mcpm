package org.hydev.mcpm.client.database;


import org.hydev.mcpm.*;

import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.*;  
import java.util.Scanner;

import javax.naming.NameNotFoundException;
import javax.swing.plaf.metal.MetalIconFactory.FileIcon16;  

public class ListAllBoundary {
    ListAllInteractor listAllInteractor;

    public List<String> listBoundary(String parameter) {
        try {
            return listAllInteractor.listAll(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
