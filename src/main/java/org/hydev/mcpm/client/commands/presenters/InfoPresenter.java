package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.interaction.ILogger;
import org.hydev.mcpm.client.models.PluginYml;

/**
 * Present plugin info command output
 */
public interface InfoPresenter
{
    /**
     * Present results
     */
    void present(PluginYml yml, ILogger log);
}
