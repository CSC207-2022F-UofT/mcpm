package org.hydev.mcpm.client.database.fetcher;

import com.github.luben.zstd.Zstd;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.hydev.mcpm.client.models.Database;
import org.hydev.mcpm.utils.HashUtils;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hydev.mcpm.Constants.JACKSON;

/**
 * A database fetcher that has a local persistent cache along and a network fallback.
 */
public class LocalDatabaseFetcher implements DatabaseFetcher {
    private final URI host;
    private final Path cacheDirectory;

    private Database localDatabase;
    private boolean enableCompression = true;

    public static final String HASH_FILE_NAME = "db.hash";
    public static final String DATABASE_FILE_NAME = "db";
    public static final String DATABASE_ZST_FILE_NAME = "db.zst";

    public static final String USER_AGENT = "MCPM Client";

    /**
     * Creates a database fetcher that syncs with a mirror.
     * The cache directory will default to .mcpm
     *
     * @param host The host URL MCPM server mirror that will be used to request database files.
     *             Example: "http://mcpm.hydev.com"
     */
    public LocalDatabaseFetcher(URI host) {
        this(host, Path.of(".mcpm/"));
    }

    /**
     * Creates a database fetcher that syncs with a mirror.
     *
     * @param host The host URL MCPM server mirror that will be used to request database files.
     *             Example: "http://mcpm.hydev.com"
     * @param cacheDirectory The directory that the cached database and hash file will be stored (as child files).
     */
    public LocalDatabaseFetcher(URI host, Path cacheDirectory) {
        this.host = host;
        this.cacheDirectory = cacheDirectory;
    }

    private ClassicHttpRequest requestTo(String path) {
        var request = new HttpGet(URI.create(host.toString() + "/" + path));

        request.addHeader("Host", host.getHost());
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Accepts", "application/json");

        return request;
    }

    @Nullable
    private String fetchHostHash() {
        try (var client = HttpClients.createDefault()) {
            return client.execute(
                requestTo(HASH_FILE_NAME),
                response -> EntityUtils.toString(response.getEntity(), "UTF-8")
            );
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    private String fetchLocalHash() {
        try {
            return Files.readString(Paths.get(cacheDirectory.toString(), HASH_FILE_NAME));
        } catch (IOException e) {
            return null;
        }
    }

    private boolean localDatabaseIsUpToDate() {
        var hostHash = fetchHostHash();
        var localHash = fetchLocalHash();

        // We can't reach the server right now, don't update.
        if (hostHash == null)
            return true;

        // Missing hash, we need to update.
        if (localHash == null)
            return false;

        return hostHash.equals(localHash);
    }

    @Nullable
    private Database fetchLocalDatabase() {
        try {
            if (localDatabase != null) {
                return localDatabase;
            }

            var file = Paths.get(cacheDirectory.toString(), DATABASE_FILE_NAME).toFile();

            var database = JACKSON.readValue(file, Database.class);

            if (database != null) {
                localDatabase = database;
            }

            return database;
        } catch (IOException e) {
            return null;
        }
    }

    private String readDatabaseFromContent(HttpEntity entity, DatabaseFetcherListener listener) throws IOException {
        long total = entity.getContentLength();

        var builder = new ByteArrayOutputStream();

        try (var stream = entity.getContent()) {
            var buffer = new byte[8096];
            long completed = 0;

            var count = stream.read(buffer);

            while (count > 0) {
                builder.write(buffer, 0, count);

                completed += count;
                listener.download(completed, total);

                count = stream.read(buffer);
            }

            listener.download(total, total);
        }

        listener.finish();

        // Decompress ZSTD
        if (this.enableCompression)
        {
            var bs = builder.toByteArray();
            bs = Zstd.decompress(bs, (int) Zstd.decompressedSize(bs));

            return new String(bs, StandardCharsets.UTF_8);
        }
        else return builder.toString(StandardCharsets.UTF_8);
    }

    @Nullable
    private Database fetchHostDatabase(DatabaseFetcherListener listener) {
        try (var client = HttpClients.createDefault()) {
            var body = client.execute(
                requestTo(enableCompression ? DATABASE_ZST_FILE_NAME : DATABASE_FILE_NAME),
                request -> readDatabaseFromContent(request.getEntity(), listener)
            );

            var database = JACKSON.readValue(body, Database.class);

            if (database != null) {
                localDatabase = database;

                var hash = new HashUtils().hash(body);

                //noinspection ResultOfMethodCallIgnored
                cacheDirectory.toFile().mkdirs();

                Files.writeString(Paths.get(cacheDirectory.toString(), DATABASE_FILE_NAME), body);
                Files.writeString(Paths.get(cacheDirectory.toString(), HASH_FILE_NAME), hash);
            }

            return database;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    @Nullable
    public Database fetchDatabase(boolean cache, DatabaseFetcherListener listener) {
        var databaseHasAlreadyBeenChecked = localDatabase != null;

        if (cache && (databaseHasAlreadyBeenChecked || localDatabaseIsUpToDate())) {
            var database = fetchLocalDatabase();

            if (database != null)
                return database;
        }

        return fetchHostDatabase(listener);
    }

    public LocalDatabaseFetcher enableCompression(boolean enableCompression)
    {
        this.enableCompression = enableCompression;
        return this;
    }
}
