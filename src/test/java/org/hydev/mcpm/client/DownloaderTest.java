package org.hydev.mcpm.client;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hydev.mcpm.client.database.mirrors.MirrorSelector.MIRROR_LIST_URL;
import static org.hydev.mcpm.utils.NetworkUtils.ping;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class DownloaderTest
{
    static boolean hasInternet = ping(MIRROR_LIST_URL) != -1;

    @Test
    void test()
    {
        assumeTrue(hasInternet);
        String link = "https://mcpm.hydev.org/db";
        File out = new File("./build/db.json");
        String link1 = "https://mcpm.hydev.org/db.zst";
        File out1 = new File("./build/db.zst");
        Downloader downloader = new Downloader().showProgress(true).threads(2);
        Map<String, File> urls = new HashMap<>();
        urls.put(link, out);
        urls.put(link1, out1);
        downloader.downloadFiles(urls);
        out.delete();
        out1.delete();
    }
}
