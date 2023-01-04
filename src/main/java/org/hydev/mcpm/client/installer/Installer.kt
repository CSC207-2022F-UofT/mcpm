package org.hydev.mcpm.client.installer

import org.hydev.mcpm.client.database.tracker.PluginTracker
import org.hydev.mcpm.client.display.presenters.Table
import org.hydev.mcpm.client.installer.input.InstallInput
import org.hydev.mcpm.client.installer.output.InstallResult
import org.hydev.mcpm.client.interaction.ILogger
import org.hydev.mcpm.client.loader.LoadBoundary
import org.hydev.mcpm.client.loader.PluginNotFoundException
import org.hydev.mcpm.client.models.PluginModel
import org.hydev.mcpm.client.search.SearchPackagesBoundary
import org.hydev.mcpm.client.search.SearchPackagesInput
import org.hydev.mcpm.client.search.SearchPackagesResult
import org.hydev.mcpm.client.search.SearchPackagesType

/**
 * Implementation to the InstallBoundary, handles installation of plugins
 */
class Installer(
    private val spigotPluginDownloader: PluginDownloader,
    private val pluginLoader: LoadBoundary?,
    private val search: SearchPackagesBoundary,
    private val local: PluginTracker
) : IInstaller {

    /**
     * Search with exceptions on error
     */
    private fun definiteSearch(type: SearchPackagesType, field: String): List<PluginModel>
    {
        val sr = runCatching { search.search(SearchPackagesInput(type, field, false)) }
            .getOrElse { throw InstallException("Plugin not found: $field") }

        if (sr.plugins.isEmpty())
            throw InstallException("Plugin not found: $field")

        return sr.plugins
    }

    /**
     * Resolve plugin models from plugin names
     */
    private suspend fun resolveNames(names: List<String>, log: ILogger): List<PluginModel>
    {
        // Name index of locally installed plugins
        val localIndex = local.listInstalled().associateBy { it.name }

        // Loop through each name
        return names.mapNotNull { name ->
            val sr = definiteSearch(SearchPackagesType.BY_NAME, name)

            if (sr.size == 1)
                return@mapNotNull sr[0]

            // Already installed
            if (name in localIndex)
            {
                // TODO: Resolve upgrade here instead of skipping
                log.print("&cPlugin $name is already installed, skipping.")
            }

            // Multiple plugins with the same name, ask user for input
            val tbl = Table(listOf("ID", "Name", "Author", "Version"),
                sr.filter { it.latest.isPresent }.map {
                    val meta = it.latest.get().meta
                    listOf(it.id.toString(), meta.name, meta.firstAuthor, meta.version)
                })
            log.print(tbl.toString())
            log.print("&6Multiple plugins matching $name found. Please choose a plugin ID: ")

            // Validate input
            while (true)
            {
                val id = runCatching { log.input() }.getOrNull()?.toLongOrNull()
                if (id == null)
                {
                    log.print("&cInvalid input. Please make sure you input the plugin ID:")
                    continue
                }

                val pl = sr.filter { it.id == id }
                if (pl.isEmpty())
                {
                    log.print("&cThe ID you typed $id isn't in the plugin list. Please try again:")
                    continue
                }

                return@mapNotNull pl[0]
            }
            null
        }
    }

    /**
     * Resolve package changes from plugin names and ids
     */
    private suspend fun resolveChanges(names: List<String>, ids: List<Long>, log: ILogger): List<PackageChange>
    {
        // Search for names
        val plugins = resolveNames(names, log).toMutableList()

        // Search for each id
        plugins += ids.map { definiteSearch(SearchPackagesType.BY_ID, it.toString())[0] }

        // Convert to package changes
        val changes = plugins.map { PackageChange(new = it.latest.get(), manual = true) }.toMutableList()

        // Resolve dependencies
        val deps = plugins.flatMap { it.latest.get().meta.depend }
        if (deps.isNotEmpty())
            changes += resolveNames(deps, log).map { PackageChange(new = it.latest.get(), manual = false) }

        return changes
    }

    /**
     * Install plugins
     */
    override suspend fun install(installInput: InstallInput, log: ILogger): List<InstallResult>
    {
        val plugins: List<PackageChange> = resolveChanges(installInput.names, installInput.ids, log)

        // Ask user to confirm
        log.print(plugins.fmt)
        if (log.input("🚀 Ready to fly? [y/N]") != "y")
            throw InstallException("🤔 Okay, cancelled")

        // Commit changes
        TODO("Actually commit changes")
    }

    // /**
    //  * Install the plugin.
    //  *
    //  * @param installInput the plugin submitted by the user for installing
    //  */
    // override fun installPlugin(installInput: InstallInput): List<InstallResult>
    // {
    //     val pluginName: Unit = installInput.name()
    //
    //     // 1. Search the name and get a list of plugins
    //     val searchResult = getSearchResult(installInput)
    //     if (searchResult.state == SearchPackagesResult.State.INVALID_INPUT)
    //     {
    //         return listOf(InstallResult(InstallResult.Type.SEARCH_INVALID_INPUT, pluginName))
    //     }
    //     if (searchResult.state == SearchPackagesResult.State.FAILED_TO_FETCH_DATABASE)
    //     {
    //         return listOf(InstallResult(InstallResult.Type.SEARCH_FAILED_TO_FETCH_DATABASE, pluginName))
    //     }
    //     if (searchResult.plugins.isEmpty())
    //     {
    //         return listOf(InstallResult(InstallResult.Type.NOT_FOUND, pluginName))
    //     }
    //
    //     // 2. Get the latest version of the plugin for the user
    //     // TODO: Let the user pick a plugin ID (Future)
    //     val pluginModel = getLastestPluginModel(searchResult)
    //     val idPluginModel = pluginModel.id
    //     val pluginVersion = pluginModel.latest.orElse(null)
    //         ?: return java.util.List.of(InstallResult(InstallResult.Type.NO_VERSION_AVAILABLE, pluginName))
    //     val results = ArrayList<InstallResult>()
    //     // 3. Installing the dependency of that plugin
    //     if (pluginVersion.meta.depend != null)
    //     {
    //         for (dependency in pluginVersion.meta.depend)
    //         {
    //             val dependencyInput = InstallInput(
    //                 dependency,
    //                 SearchPackagesType.BY_NAME,
    //                 installInput.load, false
    //             )
    //             val rec = installPlugin(dependencyInput)
    //             results.addAll(rec)
    //         }
    //     }
    //     val ifPluginDownloaded = local.findIfInLockByName(pluginName)
    //     if (!ifPluginDownloaded)
    //     {
    //         // 4. Download the plugin
    //         spigotPluginDownloader.download(pluginName, idPluginModel, pluginVersion.id)
    //         // 5. Add the installed plugin to the json file
    //         val pluginVersionId = pluginVersion.id
    //         local.addEntry(
    //             pluginName,
    //             installInput.isManuallyInstalled,
    //             pluginVersionId,
    //             idPluginModel
    //         )
    //     }
    //
    //     // 6. Load the plugin
    //     loadPlugin(pluginName, installInput.load)
    //
    //     // 7. Add success installed
    //     if (ifPluginDownloaded)
    //     {
    //         results.add(InstallResult(InstallResult.Type.PLUGIN_EXISTS, pluginName))
    //     } else
    //     {
    //         results.add(InstallResult(InstallResult.Type.SUCCESS_INSTALLED, pluginName))
    //     }
    //     return results
    // }

    /**
     * Pick the plugin id for the user
     *
     * @param searchPackagesResult Search Results of the plugin
     */
    private fun getLastestPluginModel(searchPackagesResult: SearchPackagesResult): PluginModel
    {
        var latestPluginModel = searchPackagesResult.plugins[0]
        val id = latestPluginModel.id
        for (plugin in searchPackagesResult.plugins)
        {
            if (plugin.id > id)
            {
                latestPluginModel = plugin
            }
        }
        return latestPluginModel
    }

    /**
     * Load the plugin based on user's choice
     *
     * @param name Name of the plugin
     * @param load Whether to load the plugin
     * @return Whether the plugin is successfully loaded
     */
    private fun loadPlugin(name: String, load: Boolean): Boolean
    {
        return if (!load || pluginLoader == null) false else try
        {
            pluginLoader.loadPlugin(name)
            true
        } catch (e: PluginNotFoundException)
        {
            false
        }
    }
}
