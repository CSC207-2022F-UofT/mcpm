package org.hydev.mcpm.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class Launcher
{
    public static void main(String[] args) {
        System.out.println("Meow");
        File dir = new File("plugins");
        File[] directoryListing = dir.listFiles();
        for (File child : directoryListing) {
            System.out.println(child.getName());
        }

    }
}
