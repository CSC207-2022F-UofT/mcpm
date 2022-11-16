package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.Command;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.ReloadBoundary;

import java.util.function.Consumer;

public class ReloadCommand implements Command<ReloadEntry> {
    private final ReloadBoundary reloader;

    public ReloadCommand(ReloadBoundary loader) {
        this.reloader = loader;
    }

    @Override
    public Class<ReloadEntry> type() {
        return ReloadEntry.class;
    }

    @Override
    public void run(ReloadEntry input, Consumer<String> log) {
        for (var name : input.pluginNames()) {
            try {
                reloader.reloadPlugin(name);
                log.accept(String.format("Plugin %s loaded", name));
            } catch (PluginNotFoundException e) {
                log.accept(String.format("Plugin %s not found", name));
            }
        }
    }
}
