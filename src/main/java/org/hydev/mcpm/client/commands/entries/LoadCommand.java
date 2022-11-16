package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.Command;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;

import java.util.function.Consumer;

public class LoadCommand implements Command<LoadEntry> {
    private final LoadBoundary loader;

    public LoadCommand(LoadBoundary loader) {
        this.loader = loader;
    }

    @Override
    public Class<LoadEntry> type() {
        return LoadEntry.class;
    }

    @Override
    public void run(LoadEntry input, Consumer<String> log) {
        for (var name : input.pluginNames()) {
            try {
                if (loader.loadPlugin(name)) {
                    log.accept(String.format("Plugin %s loaded", name));
                } else {
                    log.accept(String.format("Failed to load plugin %s", name));
                }
            } catch (PluginNotFoundException e) {
                log.accept(String.format("Plugin %s not found", name));
            }
        }
    }
}
