package org.hydev.mcpm.client.uninstall;

import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.loader.PluginNotFoundException;
import org.hydev.mcpm.client.loader.UnloadBoundary;

import static org.hydev.mcpm.client.uninstall.UninstallResult.State.*;


/**
 * Uninstall use case interactor
 */
public class Uninstaller implements UninstallBoundary {
    private final PluginTracker tracker;
    private final UnloadBoundary unloader;
    private final FileRemove remover;

    /**
     * Constructor for uninstaller
     *
     * @param tracker Local plugin tracker
     * @param unloader Unload implementation
     * @param remover File remover interface
     */
    public Uninstaller(PluginTracker tracker, UnloadBoundary unloader, FileRemove remover) {
        this.tracker = tracker;
        this.unloader = unloader;
        this.remover = remover;

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
                // In the minecraft server environment, we can find the exact jar of the loaded
                // plugin because java's url class loader keeps track of jar file paths.
                if (input.delete()) {
                    if (!unloader.unloadAndDeletePlugin(input.name())) {
                        return new UninstallResult(FAILED_TO_DELETE);
                    }
                }
                else {
                    unloader.unloadPlugin(input.name());
                }
            }
            catch (PluginNotFoundException ignored) { }
        }
        else if (input.delete()) {
            // When unloader is not null, it means that we are in CLI environment, so we need to
            // find the plugin of the name

            // Delete file
            var deleteResult = remover.removeFile(input.name());
            if (deleteResult == FileRemoveResult.NOT_FOUND) {
                return new UninstallResult(NOT_FOUND);
            }
            else if (deleteResult == FileRemoveResult.FAILED_TO_DELETE) {
                return new UninstallResult(FAILED_TO_DELETE);
            }
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
