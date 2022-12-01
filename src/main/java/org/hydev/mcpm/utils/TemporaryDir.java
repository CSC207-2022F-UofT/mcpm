package org.hydev.mcpm.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * This class handles temporary directories like Python's tempfile.TemporaryDirectory:
 * <p>
 * {@snippet
 * with tempfile.TemporaryDirectory() as tmpdirname:
 *      print('created temporary directory', tmpdirname)
 * # directory and contents have been removed
 * }
 * <p>
 * With this class, you can do something like:
 * <p>
 * {@snippet
 * try (var tmp = new TemporaryDir())
 * {
 *     // Do something with tmp
 * }
 * // directory and contents have been removed
 * }
 */
public class TemporaryDir implements AutoCloseable
{
    public final File path;

    /**
     * Initialize temporary dir
     */
    public TemporaryDir()
    {
        try
        {
            path = Files.createTempDirectory("mcpm-" + System.currentTimeMillis()).toFile();

            //noinspection ResultOfMethodCallIgnored
            path.mkdirs();
        }
        catch (IOException e)
        {
            // Would never happen unless the temporary directory cannot be written, in which case
            // the user would have far more important things to worry about
            throw new RuntimeException(e);
        }
    }

    /**
     * Close temporary dir
     */
    @Override
    public void close()
    {
        // Recursively delete files on exit
        try (var w = Files.walk(path.toPath()))
        {
            //noinspection ResultOfMethodCallIgnored
            w.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
