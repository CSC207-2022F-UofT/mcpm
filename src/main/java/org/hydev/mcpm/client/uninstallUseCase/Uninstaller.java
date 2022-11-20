package org.hydev.mcpm.client.uninstallUseCase;
import java.io.File;
import java.util.Arrays;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.injector.PluginNotFoundException;


public class Uninstaller implements UninstallBoundary{
    private boolean remove_depen;

    public void uninstall(String name) throws PluginNotFoundException {
        PluginLoader pil = new PluginLoader();
        pil.unloadPlugin(name);

        // Find plugin file
        // could be useful for removing dependencies (?)
        var plugins_folder = new File("plugins");
        var plugins = Arrays.stream((plugins_folder.listFiles()))
                .filter(plugin -> plugin.getName().endsWith(name+".jar"));

        File file = new File(plugins.toArray()[0].toString());

        if (file.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }


        if (remove_depen){

        }

    }
}
