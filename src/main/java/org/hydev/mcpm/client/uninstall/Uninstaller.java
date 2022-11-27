package org.hydev.mcpm.client.uninstall;

import org.hydev.mcpm.client.database.PluginTracker;
import org.hydev.mcpm.client.injector.LocalJarBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.UnloadBoundary;

import java.io.File;

import static org.hydev.mcpm.client.uninstall.UninstallResult.State.FAILED_TO_DELETE;
import static org.hydev.mcpm.client.uninstall.UninstallResult.State.SUCCESS;


/**
 * Uninstall use case interactor
 *
 * @author Anushka (https://github.com/aanushkasharma)
 */
public class Uninstaller implements UninstallBoundary {
    private final PluginTracker tracker;
    private final UnloadBoundary unloader;
    private final LocalJarBoundary jarFinder;

    public Uninstaller(PluginTracker tracker, UnloadBoundary unloader, LocalJarBoundary jarFinder) {
        this.tracker = tracker;
        this.unloader = unloader;
        this.jarFinder = jarFinder;
    }

    @Override
    public UninstallResult uninstall(UninstallInput input) throws PluginNotFoundException {
        // 1. Unload plugin
        // This will throw PluginNotFoundException if the plugin isn't loaded. If it is loaded,
        // this will return the jar of the currently loaded plugin, which is the most precise.
        // Since people may still uninstall a plugin when it's not loaded, we ignore the not
        // found error here.
        File jar = null;
        if (unloader != null) {
            try {
                jar = unloader.unloadPlugin(input.name());
            }
            catch (PluginNotFoundException ignored) { }
        }

        // 2. If it isn't loaded, find the plugin jar file in local file system
        if (jar == null) {
            // This will throw PluginNotFoundException when a plugin of the name in the file system
            // could not be found.
            jar = jarFinder.findJar(input.name());
        }

        // 3. Delete plugin jar
        if (!jar.delete()) {
            return new UninstallResult(FAILED_TO_DELETE);
        }

        // 4. Remove manually installed flag from the tracker
        tracker.removeEntry(input.name());

        // 5. Remove orphan dependencies
        if (input.recursive()) {
            var orphans = tracker.listOrphanPlugins(false);
            for (var orphan : orphans) {
                try {
                    uninstall(new UninstallInput(orphan, true));
                }
                catch (PluginNotFoundException ignored) { }
            }
        }

        return new UninstallResult(SUCCESS);
    }
}
