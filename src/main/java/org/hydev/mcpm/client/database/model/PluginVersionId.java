package org.hydev.mcpm.client.database.model;

import org.hydev.mcpm.client.models.PluginVersion;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalLong;

/**
 * Used to identify a plugin version based on many qualifications (id/user string).
 * At least one of versionId | versionString must be provided.
 * If more than one of these qualifications are provided, then plugins must match all provided entries.
 *
 * @param versionId The id for the installed PluginVersion. See PluginVersion#id.
 * @param versionString The user string for the installed PluginVersion. See PluginYML#version.
 */
public record PluginVersionId(
    OptionalLong versionId,
    @Nullable String versionString
) {
    /**
     * Checks if this object is in a valid state.
     *
     * @return True if at least one of versionId or versionString is specified.
     */
    public boolean valid() {
        return versionId.isPresent() || versionString != null;
    }

    /**
     * Returns true if the provided PluginVersion object matches this PluginVersionId object.
     *
     * @param version The provided PluginVersion object.
     * @return True if the provided versionId or versionString matches the PluginVersion object.
     */
    public boolean matches(PluginVersion version) {
        return (versionId.isEmpty() || versionId.getAsLong() == version.id())
            && (versionString == null || versionString.equals(version.meta().version()));
    }

    /**
     * Returns a PluginVersion where the only populated field is versionId.
     *
     * @param id The value for the id field.
     * @return A plugin version id object.
     */
    public static PluginVersionId byId(long id) {
        return new PluginVersionId(OptionalLong.of(id), null);
    }

    /**
     * Returns a PluginVersion where the only populated field is versionString.
     *
     * @param string The value for the string field.
     * @return A plugin version id object.
     */
    public static PluginVersionId byString(String string) {
        return new PluginVersionId(OptionalLong.empty(), string);
    }
}
