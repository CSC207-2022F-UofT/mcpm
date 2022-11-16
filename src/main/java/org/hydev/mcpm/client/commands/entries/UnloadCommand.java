package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.Command;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.UnloadBoundary;

import java.util.function.Consumer;

public class UnloadCommand implements Command<UnloadEntry> {
    private final UnloadBoundary unloader;

    public UnloadCommand(UnloadBoundary loader) {
        this.unloader = loader;
    }

    @Override
    public Class<UnloadEntry> type() {
        return UnloadEntry.class;
    }

    @Override
    public void run(UnloadEntry input, Consumer<String> log) {
        for (var name : input.pluginNames()) {
            try {
                unloader.unloadPlugin(name);

                log.accept(String.format("Plugin %s loaded", name));
            } catch (PluginNotFoundException e) {
                log.accept(String.format("Plugin %s not found", name));
            }
        }
    }
}
