package org.hydev.mcpm.client.search

import org.hydev.mcpm.client.models.PluginModel

/**
 * Search for plugins by unique id
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-03
 */
class SearcherById : Searcher
{
    private lateinit var idIndex: Map<Long, PluginModel>

    override fun getSearchList(inp: String, plugins: MutableList<PluginModel>): List<PluginModel>
    {
        // Create index
        if (!this::idIndex.isInitialized) idIndex = plugins.associateBy { it.id }

        // Return result
        return listOf(idIndex[inp.toLong()]).mapNotNull { it }
    }
}

