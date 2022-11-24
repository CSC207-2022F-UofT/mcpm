package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.InstallResult;
import org.hydev.mcpm.client.installer.input.InstallInput;

import java.util.function.Consumer;

/**
 * Controller class for the Install use case.
 */
public record InstallController(InstallBoundary boundary)
{
    /**
     * Install the plugin
     *
     * @param name Plugin name from repository
     * @param type The type of searching package
     * @param load Whether to load after installing
     * @param log Log string
     */
    public void install(String name, SearchPackagesType type, boolean load, Consumer<String> log) {
        // TODO: log error
        InstallInput input = new InstallInput(name, type, load, true);
        boundary.installPlugin(input);
        log.accept("string");
    }
}
