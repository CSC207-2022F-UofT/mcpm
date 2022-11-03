package org.hydev.mcpm.client.database.boundary;

import org.hydev.mcpm.client.database.inputs.ListPackagesInput;
import org.hydev.mcpm.client.database.inputs.ListPackagesResult;

/**
 * Defines how users can interact with a boundary to receive paginated packages.
 */
public interface ListPackagesBoundary {
    /**
     * Invoke this method to complete a list packages request.
     *
     * @param input See the ListPackagesInput record.
     * @return See the ListPackagesResult record.
     */
    ListPackagesResult list(ListPackagesInput input);
}
