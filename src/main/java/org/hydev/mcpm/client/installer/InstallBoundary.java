package org.hydev.mcpm.client.installer;


import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.database.DatabaseInteractor;
import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.results.ListPackagesResult;
import org.hydev.mcpm.client.installer.input.InstallInput;

/**
 * Interface for installing plugin to the jar file.
 *
 * @author Rena (https://github.com/thudoan1706)
 * @since 2022-11-18
 */

public interface InstallBoundary {

    // install Plugin
    void installPlugin(InstallInput installInput);
}
