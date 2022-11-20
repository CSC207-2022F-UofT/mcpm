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
    public static void main(String[] args)
    {
        System.out.println("Meow");
        String link1 = "https://sd.blackball.lv/library/Introduction_to_Algorithms_Third_Edition_(2009).pdf";
        File file1 = new File("/Users/anushkasharma/Documents/uni/Introduction_to_Algorithms_Third_Edition.pdf");
        String link2 = "https://www.iusb.edu/students/academic-success-programs/academic-centers-for-excellence/docs/Basic%20Math%20Review%20Card.pdf";
        File file2 = new File("/Users/anushkasharma/Documents/uni/Basic Math Review Card.pdf");


        Downloader downloader = new Downloader();
        Map<String, File> urls = new HashMap<>();
        urls.put(link1, file1);
        urls.put(link2, file2);
        downloader.downloadFiles(urls, true, 2);

    }
}
