package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class StoredHashMapTest
{

    @Test
    void putRemoveSave()
    {
        var fp = new File("test.meow.hashmap.json");
        if (fp.exists()) fp.delete();

        var s = new StoredHashMap<>(fp);
        s.put("a", 1);
        s.put("b", 2);
        assert s.get("a").equals(1);
        assert s.get("b").equals(2);

        var s2 = new StoredHashMap<>(fp);
        assert s2.get("a").equals(1) && s2.get("b").equals(2);
        s2.remove("a");

        var s3 = new StoredHashMap<>(fp);
        assert !s3.containsKey("a") && s3.get("b").equals(2);
        s3.remove("b", 1);
        assert s3.get("b").equals(2);
        s3.clear();
        assert !s3.containsKey("b");
        
        fp.delete();
    }
}
