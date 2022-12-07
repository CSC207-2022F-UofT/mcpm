package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.UnloadBoundary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MockUnloadBoundary implements UnloadBoundary {
    private final List<String> names = new ArrayList<>();

    private boolean throwsNotFound = false;

    @Override
    public File unloadPlugin(String name) throws PluginNotFoundException {
        names.add(name);

        if (throwsNotFound) {
            throw new PluginNotFoundException(name);
        }

        return null;
    }

    public void setThrowsNotFound(boolean notFound) {
        this.throwsNotFound = notFound;
    }

    public List<String> getNames() {
        return List.copyOf(names);
    }
}
