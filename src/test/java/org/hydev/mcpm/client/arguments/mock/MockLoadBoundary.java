package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MockLoadBoundary implements LoadBoundary {
    private final List<String> names = new ArrayList<>();

    private boolean defaultResult = true;
    private boolean throwsNotFound = false;

    @Override
    public boolean loadPlugin(String name) throws PluginNotFoundException {
        names.add(name);

        if (throwsNotFound) {
            throw new PluginNotFoundException(name);
        }

        return defaultResult;
    }

    public void setDefaultResult(boolean result) {
        this.defaultResult = result;
    }

    public void setThrowsNotFound(boolean notFound) {
        this.throwsNotFound = notFound;
    }

    public List<String> getNames() {
        return List.copyOf(names);
    }
}
