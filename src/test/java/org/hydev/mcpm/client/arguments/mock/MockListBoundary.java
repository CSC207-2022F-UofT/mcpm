package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.list.ListAllBoundary;
import org.hydev.mcpm.client.list.ListType;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;

import java.util.ArrayList;
import java.util.List;

public class MockListBoundary implements ListAllBoundary {
    private final List<ListType> types = new ArrayList<>();

    @Override
    public List<PluginYml> listAll(ListType parameter) {
        types.add(parameter);

        return List.of();
    }

    public List<ListType> getTypes() {
        return List.copyOf(types);
    }
}
