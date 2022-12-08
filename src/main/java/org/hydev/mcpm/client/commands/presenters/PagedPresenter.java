package org.hydev.mcpm.client.commands.presenters;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Presenter for paginated content
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
public interface PagedPresenter<T>
{
    /**
     * Format a specific page
     *
     * @param page Page number (page 1 should be the first page)
     * @param lines Lines per page
     * @return Formatted page
     */
    T presentPage(int page, int lines);

    /**
     * Count total pages
     *
     * @param lines Lines per page
     * @return Total amount of pages
     */
    int total(int lines);
}
