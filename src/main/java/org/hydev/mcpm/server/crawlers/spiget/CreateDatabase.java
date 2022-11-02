package org.hydev.mcpm.server.crawlers.spiget;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.utils.Hex;
import org.hydev.mcpm.client.models.Database;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Responsible for statically generating database "index" files for mcpm.
 * Used for searches, etc. To create a database use the createDatabase method
 * with a path to the .mcpm/pkgs/spiget directory generated previously by SpigetCrawler.
 */
public class CreateDatabase {
    public static final String packageStore = ".mcpm";

    /**
     * Main method generates a database in the mcpm directory.
     *
     * @param args Arguments are ignored.
     */
    public static void main(String[] args) {
        writeDatabase(new File(packageStore, "pkgs/spiget"), new File(packageStore, "db"));
    }

    /**
     * Iterates through the crawlerDirectory and assembles and writes a database object to databaseFile.
     *
     * @param crawlerDirectory Path to the .mcpm/pkgs/spiget directory generated by SpigetCrawler.
     * @param databaseFile Path to the db.json file that will be written to.
     */
    public static void writeDatabase(File crawlerDirectory, File databaseFile) {
        Database database = createDatabase(crawlerDirectory);

        if (database == null)
            return;

        try {
            new ObjectMapper().writeValue(databaseFile, database);
        } catch (IOException e) {
            e.printStackTrace();

            System.out.println("Failed to write database file.");
        }
    }

    /**
     * Returns a Database object created from the contents of the crawler directory.
     *
     * @param crawlerDirectory The directory generated by SpigetCrawler that contains package info.
     * @return A database object containing information about the valid plugins.
     */
    @Nullable
    public static Database createDatabase(File crawlerDirectory) {
        var files = crawlerDirectory.listFiles();

        if (files == null) {
            System.err.println("Missing contents of directory");

            return null;
        }

        List<PluginModel> plugins = Arrays.stream(files)
            .map(CreateDatabase::createPluginModel)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        return new Database(plugins);
    }

    @NotNull
    private static Optional<PluginModel> createPluginModel(File directory) {
        try {
            var id = Long.parseLong(directory.getName());
            var versionFiles = directory.listFiles();

            if (versionFiles == null) {
                System.err.println("Missing version directory content for plugin id " + id);

                return Optional.empty();
            }

            List<PluginVersion> versions = Arrays.stream(versionFiles)
                .map(versionDir -> createPluginVersion(id, versionDir))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

            return Optional.of(new PluginModel(id, versions));
        } catch (NumberFormatException e) {
            e.printStackTrace();

            // We don't care, let's keep going.
            return Optional.empty();
        }
    }

    @NotNull
    private static Optional<PluginVersion> createPluginVersion(long pluginId, File versionDir) {
        var metaFile = new File(versionDir, "plugin.yml");
        var jarFile = new File(versionDir, "release.jar");

        try {
            if (!metaFile.isFile()) {
                System.err.println("Missing plugin.yml file for plugin id " + pluginId);
                return Optional.empty();
            }

            if (!jarFile.isFile()) {
                System.err.println("Missing release.jar file for plugin id " + pluginId);
                return Optional.empty();
            }

            String hash;

            hash = generateHash(jarFile);

            var meta = PluginYml.fromYml(Files.readString(metaFile.toPath()));

            var versionId = Long.parseLong(versionDir.getName());

            return Optional.of(new PluginVersion(versionId, jarFile.length(), hash, meta));
        } catch (JsonProcessingException e) {
            System.out.println("Failed to parse " + metaFile.toPath());
            e.printStackTrace();

            return Optional.empty();
        } catch (NumberFormatException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }

    @NotNull
    private static String generateHash(File file) throws NoSuchAlgorithmException, IOException {
        var digest = MessageDigest.getInstance("SHA-256");

        try (var stream = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[8196];
            int count = stream.read(buffer);
            while (count > 0) {
                digest.update(buffer, 0, count);

                count = stream.read(buffer);
            }

            return Hex.encodeHexString(digest.digest());
        }
    }
}
