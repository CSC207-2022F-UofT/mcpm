package org.hydev.mcpm.client.uninstall;

import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.injector.LocalJarBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.UnloadBoundary;
import org.hydev.mcpm.client.uninstall.FileRemove;
import org.hydev.mcpm.client.loader.LocalJarBoundary;
import org.hydev.mcpm.client.loader.PluginNotFoundException;
import org.hydev.mcpm.client.loader.UnloadBoundary;

import java.io.File;

import static org.hydev.mcpm.client.uninstall.UninstallResult.State.*;


/**
 * Uninstall use case interactor
 */
public class Uninstaller implements UninstallBoundary {
    private final PluginTracker tracker;
    private final UnloadBoundary unloader;
    private final LocalJarBoundary jarFinder;

    /**
     * Constructor for uninstaller
     *
     * @param tracker Local plugin tracker
     * @param unloader Unload implementation
     * @param jarFinder Local jar finder
     */
    public Uninstaller(PluginTracker tracker, UnloadBoundary unloader, LocalJarBoundary jarFinder) {
        this.tracker = tracker;
        this.unloader = unloader;
        this.jarFinder = jarFinder;

    }

    @Override
    public UninstallResult uninstall(UninstallInput input)  {
        // 1. Unload plugin
        // This will throw PluginNotFoundException if the plugin isn't loaded. If it is loaded,
        // this will return the jar of the currently loaded plugin, which is the most precise.
        // Since people may still uninstall a plugin when it's not loaded, we ignore the not
        // found error here.
        if (unloader != null) {
            try {
                unloader.unloadPlugin(input.name());
            }
            catch (PluginNotFoundException ignored) { }
        }

        // Steps 2. + 3.
        RemoveFile fileRemover = new RemoveFile(jarFinder);

        if (fileRemover.removeFile(input.name())==1) {
            return new UninstallResult(NOT_FOUND);
        } else if (fileRemover.removeFile(input.name())==2) {
            return new UninstallResult(FAILED_TO_DELETE);
        }


        // 4. Remove manually installed flag from the tracker
        tracker.removeEntry(input.name());
        var result = new UninstallResult(SUCCESS);

        // 5. Remove orphan dependencies
        if (input.recursive()) {
            var orphans = tracker.listOrphanPlugins(false);
            for (var orphan : orphans) {
                var rec = uninstall(new UninstallInput(orphan.name(), true));

                // Recursive result
                result.dependencies().put(orphan.name(), rec.state());
                for (var pair : rec.dependencies().entrySet()) {
                    if (!result.dependencies().containsKey(pair.getKey())) {
                        result.dependencies().put(pair.getKey(), pair.getValue());
                    }
                }
            }
        }

        return result;
    }

}
