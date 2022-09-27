package org.hydev.mcpm.client.models;

import java.util.List;

/**
 * Command declared by a plugin's meta
 *
 * @param name Name to activate the command
 * @param description Description
 * @param aliases Other names
 * @param permission Required permission
 * @param usage Usage help
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public record PluginCommand(
    String name,
    String description,
    List<String> aliases,
    String permission,
    String usage
)
{
}
