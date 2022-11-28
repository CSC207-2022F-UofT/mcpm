package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TemporaryDirTest
{
    @Test
    void test() throws IOException
    {
        var t = new TemporaryDir();
        var tf = new File(t.path, "test.txt").toPath();
        Files.writeString(tf, "meow");

        assertEquals(Files.readString(tf).strip(), "meow");
        t.close();
        System.out.println(tf.toFile());
        assertFalse(tf.toFile().exists());
    }
}
