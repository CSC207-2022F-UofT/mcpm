package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a mock implementation of the LoadBoundary interface for testing.
 */
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

    /**
     * Sets the default result that this interface will return when invoked.
     *
     * @param result The default return value for the load method.
     */
    public void setDefaultResult(boolean result) {
        this.defaultResult = result;
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
