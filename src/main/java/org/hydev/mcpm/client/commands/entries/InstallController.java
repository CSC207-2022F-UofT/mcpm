package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.InstallException;
import org.hydev.mcpm.client.installer.input.InstallInput;

import java.util.function.Consumer;

public record InstallController(InstallBoundary boundary)
{
    /**
     *
     * @param name
     * @param type
     * @param load
     * @param log
     */
    public void install(String name, SearchPackagesType type, boolean load, Consumer<String> log) throws InstallException {
        InstallInput input = new InstallInput(name, type, load);
        boundary.installPlugin(input);
        log.accept("string");
        // TODO: Call install
    }
}
