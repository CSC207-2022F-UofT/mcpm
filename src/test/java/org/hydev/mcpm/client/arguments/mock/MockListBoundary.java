package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.list.ListAllBoundary;
import org.hydev.mcpm.client.list.ListType;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a mock implementation of the ListAllBoundary interface for testing.
 */
public class MockListBoundary implements ListAllBoundary {
    private final List<ListType> types = new ArrayList<>();

    @Override
    public List<PluginYml> listAll(ListType parameter) {
        types.add(parameter);

        return List.of();
    }

    /**
     * Gets a list of all types that this interface was invoked with.
     *
     * @return A list of type objects.
     */
    public List<ListType> getTypes() {
        return List.copyOf(types);
    }
}
