package org.hydev.mcpm.client.commands.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.hydev.mcpm.Constants;
import org.hydev.mcpm.client.commands.presenters.PagedPresenter;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * Controller for the page command
 */
public class PageController implements PageBoundary
{
    private static final File PERSIST_PATH = new File(Constants.CFG_PATH, "page-info.json");
    private PagedPresenter<?> pager;
    private boolean loaded = false;
    private final int pageSize;

    /**
     * Constructor, try to read the persisted pager
     */
    public PageController(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * Initialization function for lazy init
     */
    private void load()
    {
        if (loaded) return;
        if (PERSIST_PATH.isFile())
        {
            try
            {
                pager = Constants.JACKSON.readValue(PERSIST_PATH, new TypeReference<>() {});
            }
            catch (IOException ignored)
            {
                // Persistence doesn't work in the minecraft environment since libraries are loaded in a different
                // class loader as the plugin. However, there is no need to persist because the plugin shouldn't be
                // unloaded from memory when a command finishes running, so the error is ignored.
            }
        }
        loaded = true;
    }

    /**
     * Get a page
     *
     * @param page Page number
     * @return Page content
     */
    @Nullable
    public String formatPage(int page)
    {
        load();
        if (pager == null) return null;
        return StringUtils.stripEnd(pager.presentPage(page, pageSize).toString(), "\n") +
            String.format("&r\n(Page %s / %s : You can type /mcpm page <page number> to view the next page)",
                page, pager.total(pageSize));
    }

    @Override
    public int pageSize()
    {
        return pageSize;
    }

    /**
     * Set pager
     *
     * @param pager Pager object
     */
    public void store(PagedPresenter<?> pager)
    {
        this.pager = pager;
        this.loaded = true;

        // Persist
        try
        {
            Constants.JACKSON.writeValue(PERSIST_PATH, pager);
        }
        catch (IOException e)
        {
            System.err.println("Failed to save pagination detials.");
            e.printStackTrace();
        }
    }
}
