package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.updater.UpdateBoundary;
import org.hydev.mcpm.client.updater.UpdateInput;
import org.hydev.mcpm.client.updater.UpdateResult;

import java.util.ArrayList;
import java.util.List;

public class MockUpdateBoundary implements UpdateBoundary {
    private final List<UpdateInput> inputs = new ArrayList<>();

    @Override
    public UpdateResult update(UpdateInput input) {
        inputs.add(input);

        return UpdateResult.by(UpdateResult.State.SUCCESS);
    }

    public List<UpdateInput> getInputs() {
        return List.copyOf(inputs);
    }
}
