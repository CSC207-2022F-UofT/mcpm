package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.tracker.SuperPluginTracker;
import org.hydev.mcpm.client.display.presenters.InstallPresenter;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.local.LocalDatabaseFetcher;
import org.hydev.mcpm.client.local.SuperLocalPluginTracker;
import org.hydev.mcpm.client.search.SearchInteractor;
import org.hydev.mcpm.client.search.SearchPackagesBoundary;
import org.hydev.mcpm.utils.ColorLogger;

import java.net.URI;

public class InstallSetUp {
    private static final Downloader downloader = new Downloader();
    private static final PluginLoader loader = null;
    private static final URI host = URI.create("https://mcpm.hydev.org");
    private static final DatabaseFetcher fetcher = new LocalDatabaseFetcher(() -> host);
    private static final SuperPluginTracker superTracker = new SuperLocalPluginTracker();
    private static final SearchPackagesBoundary searcher = new SearchInteractor(fetcher);
    private static final SpigotPluginDownloader spigotPluginDownloader =
                                                                    new SpigotPluginDownloader(downloader, () -> host);
    private static InstallInteractor installInteractor;
    private static InstallPresenter installPresenter;
    private static ColorLogger colorLogger;
    private static InstallSetUp installSetUp = new InstallSetUp();

    private InstallSetUp() {
        // Succeed in fetching database
        installInteractor = new InstallInteractor(spigotPluginDownloader,
                                                  loader,
                                                  searcher,
                                                  superTracker);
        installPresenter = new InstallPresenter();
        colorLogger = new ColorLogger();
    }

    public static InstallSetUp getInstaller() {
        return installSetUp;
    }

    public InstallInteractor getInstallInteractor() {
        return installInteractor;
    }

    public InstallPresenter getInstallPresenter() {
        return installPresenter;
    }

    public ColorLogger getColorLogger() {
        return colorLogger;
    }

}
