package org.hydev.mcpm.utils;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Utilities for JVM reflections
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-29
 */
public class ReflectionUtils
{
    /**
     * Read the value of a private instance variable
     *
     * @param obj Object to read from
     * @param fieldName Name of the variable
     * @param type Type of the result
     * @return Value of the private instance variable, or null if it cannot be read
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getPrivateField(@NotNull Object obj, @NotNull String fieldName,
                                                  @NotNull TypeToken<T> type)
    {
        try
        {
            var field = findField(obj.getClass(), fieldName);
            if (field == null) return Optional.empty();
            field.setAccessible(true);
            return Optional.ofNullable((T) type.getRawType().cast(field.get(obj)));
        }
        catch (IllegalAccessException e)
        {
            return Optional.empty();
        }
    }

    /**
     * Set the value of a private instance variable
     *
     * @param obj Object to modify
     * @param fieldName Name of the variable
     * @param value New value to set
     * @return True if set successfully, or false if failed
     */
    public static boolean setPrivateField(@NotNull Object obj, @NotNull String fieldName, Object value)
    {
        try
        {
            var field = findField(obj.getClass(), fieldName);
            if (field == null) return false;
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        }
        catch (IllegalAccessException e)
        {
            return false;
        }
    }

    /**
     * Find field in all parent classes, return null if not found
     *
     * @param cls Any class
     * @param name Name of the field
     * @return Field or null
     */
    public static @Nullable Field findField(@NotNull Class<?> cls, @NotNull String name)
    {
        while (cls != Object.class)
        {
            try {
                return cls.getDeclaredField(name);
            }
            catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        return null;
    }
}
