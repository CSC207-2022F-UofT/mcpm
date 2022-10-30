package org.hydev.mcpm.utils;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        @SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
        private final List<String> field = new ArrayList<>(List.of("meow", "qwq"));
    }

    @Test
    void getPrivateField()
    {
        var a = new A();
        var f = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){}).orElseThrow();
        assert f.get(0).equals("meow");
        assert f.get(1).equals("qwq");

        // The obtained field is a pointer
        f.set(1, "wolf");
        var newF = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){}).orElseThrow();
        assert newF.get(1).equals("wolf");
    }

    @Test
    void setPrivateField()
    {
        var a = new A();
        var f = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){}).orElseThrow();
        assert f.get(0).equals("meow");
        assert ReflectionUtils.setPrivateField(a, "field", List.of("wolf"));
        assert f.get(0).equals("meow");

        var newF = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){}).orElseThrow();
        assert newF.get(0).equals("wolf");
    }
}
