package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StoredHashMapTest
{
    @Test
    @Tag("IntegrationTest")
    void putRemoveSave()
    {
        var fp = new File("test.meow.hashmap.json");
        if (fp.exists()) {
            //noinspection ResultOfMethodCallIgnored
            fp.delete();
        }

        var s = new StoredHashMap<>(fp);
        s.put("a", 1);
        s.put("b", 2);
        assertEquals(s.get("a"), 1);
        assertEquals(s.get("b"), 2);

        var s2 = new StoredHashMap<>(fp);
        assertEquals(s2.get("a"), 1);
        assertEquals(s2.get("b"), 2);
        s2.remove("a");

        var s3 = new StoredHashMap<>(fp);
        assertFalse(s3.containsKey("a"));
        assertEquals(s3.get("b"), 2);
        s3.remove("b", 1);
        assertEquals(s3.get("b"), 2);
        s3.clear();
        //noinspection ConstantConditions
        assertFalse(s3.containsKey("b"));

        //noinspection ResultOfMethodCallIgnored
        fp.delete();
    }
}
