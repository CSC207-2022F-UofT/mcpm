package org.hydev.mcpm.utils;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import static org.hydev.mcpm.Constants.JACKSON;

/**
 * A hashmap that's stored in a path
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-03
 */
public class StoredHashMap<K, V> extends HashMap<K, V>
{
    private final File path;

    /**
     * Create a stored hashmap. (If the file exists, it will read from the file)
     *
     * @param path Local storage path
     */
    public StoredHashMap(File path)
    {
        this.path = path;

        try
        {
            // If file doesn't exist, create empty file
            if (!path.isFile())
            {
                Files.writeString(path.toPath(), "{}");
            }

            // Now the file must exist, read and parse as json object
            var map = JACKSON.readValue(path, new TypeReference<HashMap<K, V>>() {});
            this.putAll(map);
        }
        catch (IOException e)
        {
            // Would never happen unless file permissions are misconfigured or JSON is corrupt.
            // In both cases, it is something that must be resolved by the developer
            throw new RuntimeException(e);
        }
    }

    /**
     * Save the hashmap
     */
    public void save()
    {
        try
        {
            JACKSON.writeValue(path, this);
        }
        catch (IOException e)
        {
            System.err.printf("Unable to save HashMap %s: %s\n", path, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public V put(K key, V value)
    {
        var ret = super.put(key, value);
        save();
        return ret;
    }

    @Override
    public boolean remove(Object key, Object value)
    {
        var ret = super.remove(key, value);
        save();
        return ret;
    }

    @Override
    public V remove(Object key)
    {
        var ret = super.remove(key);
        save();
        return ret;
    }

    @Override
    public void clear()
    {
        super.clear();
        save();
    }
}
