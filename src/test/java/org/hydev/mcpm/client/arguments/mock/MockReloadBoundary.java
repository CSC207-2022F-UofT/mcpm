package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.ReloadBoundary;

import java.util.List;
import java.util.ArrayList;

public class MockReloadBoundary implements ReloadBoundary {
    private final List<String> names = new ArrayList<>();

    private boolean throwsNotFound = false;

    @Override
    public void reloadPlugin(String name) throws PluginNotFoundException {
        names.add(name);

        if (throwsNotFound) {
            throw new PluginNotFoundException(name);
        }
    }

    public void setThrowsNotFound(boolean notFound) {
        this.throwsNotFound = notFound;
    }

    public List<String> getNames() {
        return List.copyOf(names);
    }
}
