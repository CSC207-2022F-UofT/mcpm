package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.inputs.ListPackagesInput;
import org.hydev.mcpm.client.database.results.ListPackagesResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

/**
 * Tests the DatabaseInteractor's list method.
 */
public class DatabaseInteractorListTest {
    private static DatabaseInteractor emptyInteractor;
    private static DatabaseInteractor smallInteractor;

    /**
     * Create relevant interactors for tests.
     */
    @BeforeAll
    public static void setup() {
        emptyInteractor = PluginMockFactory.interactor(List.of());

        smallInteractor = PluginMockFactory.interactor(List.of(
            PluginMockFactory.model(1),
            PluginMockFactory.model(2, "hello"),
            PluginMockFactory.model(3, "test")
        ));
    }

    /**
     * Test the list command for an empty database (nothing to list).
     */
    @Test
    public void listEmptyPlugins() {
        var input = new ListPackagesInput(20, 0, false);
        var result = emptyInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.NO_SUCH_PAGE);
    }

    /**
     * Test the list command for listing more commands than available in the database.
     * Ex. This is a page boundary overflow.
     */
    @Test
    public void listOverPageBoundaryPluginCount() {
        var input = new ListPackagesInput(20, 0, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.plugins().size(), 3);
    }

    /**
     * Test the list command for a request of a page that does not exist.
     */
    @Test
    public void listNoSuchPage() {
        var input = new ListPackagesInput(20, 1, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.NO_SUCH_PAGE);
    }

    /**
     * Test the list command for the correct total plugin count of a 2-page database.
     */
    @Test
    public void listPageSubsetHasCorrectTotalPlugins() {
        var input = new ListPackagesInput(2, 0, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.totalPlugins(), 3);
    }

    /**
     * Test the list command for the correct first page plugin count of a 2-page database.
     */
    @Test
    public void listPageSubsetHasCorrectPluginCount() {
        var input = new ListPackagesInput(2, 0, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.plugins().size(), 2);
    }

    /**
     * Test the list command for the correct plugins of the first page of a 2-page database.
     */
    @Test
    public void listPageSubset() {
        var input = new ListPackagesInput(2, 0, false);
        var result = smallInteractor.list(input);

        var correctIds = Set.of(1, 2);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.pageNumber(), 0);
        assertTrue(correctIds.stream().allMatch(
            id -> result.plugins().stream().anyMatch(plugin -> plugin.id() == id)
        ));
    }

    /**
     * Test the list command for the correct plugins of the second page of a 2-page database.
     */
    @Test
    public void listNextPageSubset() {
        var input = new ListPackagesInput(2, 1, false);
        var result = smallInteractor.list(input);

        var correctIds = Set.of(3);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.pageNumber(), 1);
        assertTrue(correctIds.stream().allMatch(
            id -> result.plugins().stream().anyMatch(plugin -> plugin.id() == id)
        ));
    }

    /**
     * Test the list command for the correct plugin count of the second page of a 2-page database.
     */
    @Test
    public void listNextPageSubsetHasCorrectPluginCount() {
        var input = new ListPackagesInput(2, 1, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.plugins().size(), 1);
    }

    /**
     * Test the list command for an invalid input during a negative page number.
     */
    @Test
    public void listNegativePageNumber() {
        var input = new ListPackagesInput(2, -1, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.INVALID_INPUT);
    }

    /**
     * Test the list command to return all plugins with a negative items per page.
     */
    @Test
    public void listNegativeItemsPerPage() {
        var input = new ListPackagesInput(-1, 0, false);
        var result = smallInteractor.list(input);

        var correctIds = Set.of(1, 2, 3);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertTrue(correctIds.stream().allMatch(
            id -> result.plugins().stream().anyMatch(plugin -> plugin.id() == id)
        ));
    }
}
