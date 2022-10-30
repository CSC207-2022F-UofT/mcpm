package org.hydev.mcpm.utils;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-29
 */
class ReflectionUtilsTest
{
    private static class A
    {
        private final List<String> field = Arrays.asList("meow", "qwq");
    }

    @Test
    void getPrivateField()
    {
        var a = new A();
        var f = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){});
        assert f != null;
        assert f.get(0).equals("meow");
        assert f.get(1).equals("qwq");

        // The obtained field is a pointer
        f.set(1, "wolf");
        var newF = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){});
        assert newF != null;
        assert newF.get(1).equals("wolf");
    }
}
