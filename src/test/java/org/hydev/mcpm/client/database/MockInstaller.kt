package org.hydev.mcpm.client.database

import org.hydev.mcpm.client.database.tracker.PluginTracker
import org.hydev.mcpm.client.installer.IInstaller
import org.hydev.mcpm.client.installer.input.InstallInput
import org.hydev.mcpm.client.installer.output.InstallResult
import org.hydev.mcpm.client.interaction.ILogger
import org.hydev.mcpm.client.models.PluginModel
import org.hydev.mcpm.client.models.PluginVersion

/**
 * Creates a new MockInstaller object.
 *
 * @param plugins A list of plugins to lookup plugin related information about (to pass to PluginTracker).
 * @param tracker A plugin tracker to query whether a plugin is installed.
 * @param defaultResult Determines what the installer should return when installPlugin is invoked.
 */
class MockInstaller(
    private val plugins: List<PluginModel>,
    private val tracker: PluginTracker,
    private val defaultResult: InstallResult.Type
) : IInstaller
{
    private val requested: MutableSet<String> = HashSet()

    /**
     * Returns a list of all plugin names that were passed to installPlugin (and succeeded).
     *
     * @return A list of plugin names.
     */
    fun getRequested(): Set<String>
    {
        return HashSet(requested)
    }

    override suspend fun install(installInput: InstallInput, log: ILogger): List<InstallResult>
    {
        // TODO: Multiple plugins
        val name = installInput.names[0]
        if (tracker.findIfInLockByName(name)) return listOf(
            InstallResult(
                InstallResult.Type.PLUGIN_EXISTS,
                name
            )
        )
        val model = plugins.stream()
            .filter { plugin: PluginModel ->
                plugin.latest
                    .map { x: PluginVersion -> x.meta != null && name.equals(x.meta.name) }
                    .orElse(false)
            }.findFirst()
        val modelId = model.map(PluginModel::id).orElse(0L)
        val versionId = model.map { x: PluginModel ->
            x.latest
                .map(PluginVersion::id).orElse(0L)
        }.orElse(0L)
        requested.add(name)
        tracker.addEntry(name, true, versionId, modelId)
        return listOf(InstallResult(defaultResult, name))
    }
}
