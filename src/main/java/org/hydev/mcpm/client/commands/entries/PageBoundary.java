package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.presenter.PagedPresenter;

import javax.annotation.Nullable;

/**
 * Page controller boundary
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-24
 */
public interface PageBoundary
{
    /**
     * Getter for page size
     *
     * @return Lines per page
     */
    int pageSize();

    /**
     * Get a printable formatted page
     *
     * @param page Page number
     * @return Page content
     */
    @Nullable
    String formatPage(int page);

    /**
     * Persist a specific page presenter
     *
     * @param pager Page presenter
     */
    void store(PagedPresenter<?> pager);
}
