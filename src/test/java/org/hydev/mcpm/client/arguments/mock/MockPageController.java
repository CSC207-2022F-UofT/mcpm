package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.commands.controllers.PageBoundary;
import org.hydev.mcpm.client.commands.presenters.PagedPresenter;
import org.jetbrains.annotations.Nullable;

/**
 * Provides a mock implementation of the PageBoundary interface for testing.
 */
public class MockPageController implements PageBoundary {
    @Override
    public int pageSize() {
        return 0;
    }

    @Nullable
    @Override
    public String formatPage(int page) {
        return "Mock Format String";
    }

    @Override
    public void store(PagedPresenter<?> pager) {
        /* do nothing */
    }
}
