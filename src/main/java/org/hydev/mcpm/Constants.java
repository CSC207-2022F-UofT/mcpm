package org.hydev.mcpm;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

/**
 * Global constants
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-03
 */
public class Constants
{
    public static final ObjectMapper JACKSON = new ObjectMapper()
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false).enable(INDENT_OUTPUT);
}
