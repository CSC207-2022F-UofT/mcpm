package org.hydev.mcpm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_INVALID_SUBTYPE;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

/**
 * Global constants
 */
public class Constants
{
    public static final ObjectMapper JACKSON = new ObjectMapper()
        .disable(FAIL_ON_UNKNOWN_PROPERTIES).enable(INDENT_OUTPUT);

    public static final ObjectMapper YML = new ObjectMapper(new YAMLFactory())
        .disable(FAIL_ON_UNKNOWN_PROPERTIES).disable(FAIL_ON_INVALID_SUBTYPE)
        .enable(ACCEPT_SINGLE_VALUE_AS_ARRAY).enable(UNWRAP_SINGLE_VALUE_ARRAYS)
        .enable(INDENT_OUTPUT);

    public static final File CFG_PATH = new File(System.getProperty("user.home"), ".config/mcpm");
}
