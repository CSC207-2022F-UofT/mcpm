package org.hydev.mcpm.client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Command declared by a plugin's meta
 *
 * @param description Description
 * @param aliases Other names
 * @param permission Required permission
 * @param usage Usage help
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PluginCommand(
    String description,
    List<String> aliases,
    String permission,
    String usage
)
{
}
