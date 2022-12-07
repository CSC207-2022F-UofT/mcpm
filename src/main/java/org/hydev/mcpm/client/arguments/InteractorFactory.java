package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.DatabaseManager;
import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.database.mirrors.MirrorSelectBoundary;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.display.progress.ProgressBarFetcherListener;
import org.hydev.mcpm.client.export.ExportInteractor;
import org.hydev.mcpm.client.export.ExportPluginsBoundary;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.LocalJarBoundary;
import org.hydev.mcpm.client.injector.LocalJarFinder;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.injector.ReloadBoundary;
import org.hydev.mcpm.client.injector.UnloadBoundary;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.InstallInteractor;
import org.hydev.mcpm.client.installer.PluginDownloader;
import org.hydev.mcpm.client.installer.SpigotPluginDownloader;
import org.hydev.mcpm.client.list.ListAllBoundary;
import org.hydev.mcpm.client.list.ListAllInteractor;
import org.hydev.mcpm.client.local.LocalDatabaseFetcher;
import org.hydev.mcpm.client.local.LocalPluginTracker;
import org.hydev.mcpm.client.local.MirrorSelector;
import org.hydev.mcpm.client.matcher.MatchPluginsBoundary;
import org.hydev.mcpm.client.matcher.MatchPluginsInteractor;
import org.hydev.mcpm.client.search.SearchInteractor;
import org.hydev.mcpm.client.search.SearchPackagesBoundary;
import org.hydev.mcpm.client.uninstall.UninstallBoundary;
import org.hydev.mcpm.client.uninstall.Uninstaller;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.updater.CheckForUpdatesInteractor;
import org.hydev.mcpm.client.updater.UpdateBoundary;
import org.hydev.mcpm.client.updater.UpdateInteractor;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Default factory for interactor.
 * This handles caching the various objects used for interactors, and is on the highest level of CA.
 */
public class InteractorFactory implements InteractorFactoryBoundary {
    // Lazy solution, not going to store variables.
    private final Map<Class<?>, Object> localCache = new HashMap<>();
    private final boolean server;

    public InteractorFactory(boolean server) {
        this.server = server;
    }

    /**
     * Returns an object of type `type` that it previously returned with an identical `type`.
     * Otherwise, it invokes `create` and returns the value of that.
     *
     * @param type The type of the object to cache.
     * @param create A creation method if this object is not already cached.
     * @param <T> The type of the cached object.
     * @return An object of type T.
     */
    private <T> T cache(Class<T> type, Supplier<T> create) {
        var value = localCache.getOrDefault(type, null);

        if (value == null) {
            value = create.get();
            localCache.put(type, value);
        }

        return type.cast(value);
    }

    @Override
    public MirrorSelectBoundary mirrorSelector() {
        return cache(MirrorSelector.class, MirrorSelector::new);
    }

    @Override
    public DatabaseFetcher databaseFetcher() {
        var mirror = mirrorSelector();

        return cache(LocalDatabaseFetcher.class, () -> new LocalDatabaseFetcher(mirror.selectedMirrorSupplier()));
    }

    @Override
    public PluginTracker pluginTracker() {
        var lockPath = "plugins/mcpm.lock.json";
        var pluginsDirectory = "plugins";

        return cache(LocalPluginTracker.class, () -> new LocalPluginTracker(lockPath, pluginsDirectory));
    }

    @Override
    public LocalJarBoundary jarBoundary() {
        return cache(LocalJarFinder.class, LocalJarFinder::new);
    }

    private @Nullable PluginLoader pluginLoader() {
        if (server) {
            var jarFinder = jarBoundary();

            return cache(PluginLoader.class, () -> new PluginLoader(jarFinder));
        }

        return null;
    }

    @Override
    public LoadBoundary loadBoundary() {
        return pluginLoader();
    }

    @Override
    public ReloadBoundary reloadBoundary() {
        return pluginLoader();
    }

    @Override
    public UnloadBoundary unloadBoundary() {
        return pluginLoader();
    }

    @Override
    public DatabaseFetcherListener fetcherListener() {
        return cache(DatabaseFetcherListener.class, ProgressBarFetcherListener::new);
    }

    @Override
    public SearchPackagesBoundary searchBoundary() {
        var fetcher = databaseFetcher();
        var listener = fetcherListener();

        return cache(SearchInteractor.class, () -> new SearchInteractor(fetcher, listener));
    }

    @Override
    public PluginDownloader pluginDownloader() {
        var mirror = mirrorSelector();

        return cache(
            SpigotPluginDownloader.class,
            () -> new SpigotPluginDownloader(new Downloader(), mirror.selectedMirrorSupplier())
        );
    }

    @Override
    public InstallBoundary installBoundary() {
        var tracker = pluginTracker();
        var searcher = searchBoundary();
        var downloader = pluginDownloader();
        var manager = new DatabaseManager(tracker, searcher);
        var loader = loadBoundary();

        return cache(InstallInteractor.class, () -> new InstallInteractor(downloader, manager, loader));
    }

    @Override
    public MatchPluginsBoundary matchBoundary() {
        var fetcher = databaseFetcher();
        var listener = fetcherListener();

        return cache(MatchPluginsInteractor.class, () -> new MatchPluginsInteractor(fetcher, listener));
    }

    @Override
    public CheckForUpdatesBoundary checkForUpdatesBoundary() {
        var matcher = matchBoundary();

        return cache(CheckForUpdatesInteractor.class, () -> new CheckForUpdatesInteractor(matcher));
    }

    @Override
    public UpdateBoundary updateBoundary() {
        var checker = checkForUpdatesBoundary();
        var installer = installBoundary();
        var tracker = pluginTracker();

        return cache(UpdateInteractor.class, () -> new UpdateInteractor(checker, installer, tracker));
    }

    @Override
    public ExportPluginsBoundary exportBoundary() {
        var tracker = pluginTracker();

        return cache(ExportInteractor.class, () -> new ExportInteractor(tracker));
    }

    @Override
    public ListAllBoundary listBoundary() {
        var tracker = pluginTracker();
        var checkForUpdates = checkForUpdatesBoundary();

        return cache(ListAllInteractor.class, () -> new ListAllInteractor(tracker, checkForUpdates));
    }

    @Override
    public UninstallBoundary uninstallBoundary() {
        var tracker = pluginTracker();
        var unload = unloadBoundary(); // ...er
        var boundary = jarBoundary();

        return cache(Uninstaller.class, () -> new Uninstaller(tracker, unload, boundary));
    }
}
