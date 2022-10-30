package org.hydev.mcpm.utils;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @return Value of the private instance variable, or null if it cannot be read
     */
    @SuppressWarnings("unchecked")
    public static <T> @Nullable T getPrivateField(@NotNull Object obj, @NotNull String fieldName, TypeToken<T> type)
    {
        try
        {
            var field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) type.getRawType().cast(field.get(obj));
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            return null;
        }
    }
}
