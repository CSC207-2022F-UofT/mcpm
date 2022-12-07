package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.search.SearchPackagesBoundary;
import org.hydev.mcpm.client.search.SearchPackagesInput;
import org.hydev.mcpm.client.search.SearchPackagesResult;

import java.util.ArrayList;
import java.util.List;

public class MockSearchBoundary implements SearchPackagesBoundary {
    private final List<SearchPackagesInput> inputs = new ArrayList<>();

    @Override
    public SearchPackagesResult search(SearchPackagesInput input) {
        inputs.add(input);

        return SearchPackagesResult.by(SearchPackagesResult.State.SUCCESS);
    }

    public List<SearchPackagesInput> getInputs() {
        return List.copyOf(inputs);
    }
}
