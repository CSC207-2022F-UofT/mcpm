package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.UnloadBoundary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a mock implementation of the UnloadBoundary interface for testing.
 */
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

    /**
     * Sets whether this interface should throw an exception when invoked.
     *
     * @param notFound If true, this interface will throw an exception on invocation.
     */
    public void setThrowsNotFound(boolean notFound) {
        this.throwsNotFound = notFound;
    }

    /**
     * Gets a list of all names that this interface was invoked with.
     *
     * @return A list of strings that represent plugin names.
     */
    public List<String> getNames() {
        return List.copyOf(names);
    }
}
