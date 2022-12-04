package org.hydev.mcpm.utils;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class provides tests for the reflection related tools we use when hot reloading plugins.
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
        assertEquals(f, Arrays.asList("meow", "qwq"));
    }

    @Test
    void getPrivateFieldMutation()
    {
        // Show that the obtained field is a pointer, and its contents can be mutated
        var a = new A();
        var f = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){}).orElseThrow();
        f.set(1, "wolf");

        var newF = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){}).orElseThrow();
        assertEquals(newF.get(1), "wolf");
    }

    @Test
    void setPrivateField()
    {
        var a = new A();
        var f = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){}).orElseThrow();
        assertEquals(f.get(0), "meow");
        assertTrue(ReflectionUtils.setPrivateField(a, "field", List.of("wolf")));
        assertEquals(f.get(0), "meow");

        var newF = ReflectionUtils.getPrivateField(a, "field", new TypeToken<List<String>>(){}).orElseThrow();
        assertEquals(newF.get(0), "wolf");
    }
}
