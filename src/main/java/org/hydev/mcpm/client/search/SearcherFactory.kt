package org.hydev.mcpm.client.search

import org.hydev.mcpm.client.search.SearchPackagesType.*

/**
 * Factory that produces Searcher objects based on the input type.
 *
 */
class SearcherFactory
{
    private val nameSearcher: SearcherByName by lazy { SearcherByName() }
    private val keywordSearcher: SearcherByKeyword by lazy { SearcherByKeyword() }
    private val commandSearcher: SearcherByCommand by lazy { SearcherByCommand() }
    private val idSearcher: SearcherById by lazy { SearcherById() }

    private val map = mapOf(BY_NAME to nameSearcher, BY_COMMAND to commandSearcher,
        BY_KEYWORD to keywordSearcher, BY_ID to idSearcher)

    /**
     * Returns the new searcher object based on the input type.
     *
     * @param input Contains the search type in particular. See SearchPackagesInput for details.
     */
    fun createSearcher(input: SearchPackagesInput) = map[input.type]
}
