package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

class HashUtilsTest
{
    HashUtils hashUtils = new HashUtils();

    @Test
    void hashString()
    {
        assert hashUtils.hash("hello world")
            .equals("b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9");
    }

    @Test
    void hashFile() throws IOException
    {
        assert hashUtils.hash(Objects.requireNonNull(GeneralUtils.getResourceFile("test-plugin-activelist.jar")))
            .equals("6b51b4a80419843f522f0a612288c7f50cf405f5ab7dd9bb1d050cc6e80a725f");
    }

    @Test
    void constructorFailTest()
    {
        try
        {
            var a = new HashUtils("a nonexistant algorithm");
            assert false;
        }
        catch (NoSuchAlgorithmException ignored) { }
    }
}
