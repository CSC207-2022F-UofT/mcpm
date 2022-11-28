package org.hydev.mcpm.client.database.model;

import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalLong;

/**
 * Used to identify a plugin based on many qualifications (id/name/main class).
 * At least one of pluginId | pluginName | pluginMain must be provided.
 * If more than one of these qualifications are provided, then plugins must match all provided entries.
 *
 * @param pluginId The plugin id. See PluginModel#id.
 * @param pluginName The plugin name. See PluginYML#name.
 * @param pluginMain The plugin main class. See PluginYML#main.
 */
public record PluginModelId(
    // No sum types.
    OptionalLong pluginId,
    @Nullable String pluginName,
    @Nullable String pluginMain
) {
    /**
     * Checks if this object is in a valid state.
     *
     * @return True if at least one of pluginId or pluginName is pluginMain.
     */
    public boolean valid() {
        return pluginId.isPresent() || pluginName != null || pluginMain != null;
    }

    /**
     * Returns true if the provided PluginModel object matches this PluginModelId object.
     *
     * @param model The provided PluginModel object.
     * @return True if the provided pluginId, pluginName or pluginString matches the PluginModel object.
     */
    public boolean matches(PluginModel model) {
        if (pluginId.isPresent() && pluginId.getAsLong() != model.id()) {
            return false;
        }

        var optionalMeta = model.getLatestPluginVersion().map(PluginVersion::meta);

        if (optionalMeta.isEmpty()) {
            return pluginName == null && pluginMain == null;
        }

        var meta = optionalMeta.get();

        return (pluginName == null || pluginName.equals(meta.name()))
            && (pluginMain == null || pluginMain.equals(meta.main()));
    }

    /**
     * Returns a PluginModelId where the only populated field is pluginId.
     *
     * @param id The value for the id field.
     * @return A plugin id object.
     */
    public static PluginModelId byId(long id) {
        return new PluginModelId(OptionalLong.of(id), null, null);
    }

    /**
     * Returns a PluginModelId where the only populated field is pluginName.
     *
     * @param name The value for the name field.
     * @return A plugin id object.
     */
    public static PluginModelId byName(String name) {
        return new PluginModelId(OptionalLong.empty(), name, null);
    }

    /**
     * Returns a PluginModelId where the only populated field is pluginMain.
     *
     * @param main The value for the main field.
     * @return A plugin id object.
     */
    public static PluginModelId byMain(String main) {
        return new PluginModelId(OptionalLong.empty(), null, main);
    }
}
