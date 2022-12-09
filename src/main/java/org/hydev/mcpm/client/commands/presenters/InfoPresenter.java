package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.models.PluginYml;

import java.util.function.Consumer;

/**
 * Present plugin info command output
 */
public interface InfoPresenter
{
    /**
     * Present results
     */
    void present(PluginYml yml, Consumer<String> log);
}
