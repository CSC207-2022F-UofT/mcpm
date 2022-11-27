package org.hydev.mcpm.client.updater;

import java.util.List;

public record UpdateInput(
    List<String> pluginNames,
    boolean load,
    boolean noCache
) {
    public boolean updateAll() {
        return pluginNames.isEmpty();
    }
}
