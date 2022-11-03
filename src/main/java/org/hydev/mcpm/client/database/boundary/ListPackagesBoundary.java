package org.hydev.mcpm.client.database.boundary;

import org.hydev.mcpm.client.database.inputs.ListPackagesInput;
import org.hydev.mcpm.client.database.inputs.ListPackagesResult;

public interface ListPackagesBoundary {
    ListPackagesResult list(ListPackagesInput input);
}
