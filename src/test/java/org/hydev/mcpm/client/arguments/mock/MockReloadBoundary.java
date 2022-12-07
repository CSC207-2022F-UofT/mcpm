package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.ReloadBoundary;

import java.util.List;
import java.util.ArrayList;

/**
 * Provides a mock implementation of the ReloadBoundary interface for testing.
 */
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
