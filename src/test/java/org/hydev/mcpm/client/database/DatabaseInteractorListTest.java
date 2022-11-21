package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.inputs.ListPackagesInput;
import org.hydev.mcpm.client.database.results.ListPackagesResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

public class DatabaseInteractorListTest {
    static DatabaseInteractor emptyInteractor;
    static DatabaseInteractor smallInteractor;

    @BeforeAll
    static void setup() {
        emptyInteractor = PluginMockFactory.interactor(List.of());

        smallInteractor = PluginMockFactory.interactor(List.of(
            PluginMockFactory.model(1),
            PluginMockFactory.model(2, "hello"),
            PluginMockFactory.model(3,"test")
        ));
    }

    @Test
    void listEmptyPlugins() {
        var input = new ListPackagesInput(20, 0, false);
        var result = emptyInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.NO_SUCH_PAGE);
    }

    @Test
    void listOverPageBoundaryPluginCount() {
        var input = new ListPackagesInput(20, 0, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.plugins().size(), 3);
    }

    @Test
    void listNoSuchPage() {
        var input = new ListPackagesInput(20, 1, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.NO_SUCH_PAGE);
    }

    @Test
    void listPageSubsetHasCorrectTotalPlugins() {
        var input = new ListPackagesInput(2, 0, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.totalPlugins(), 3);
    }

    @Test
    void listPageSubsetHasCorrectPluginCount() {
        var input = new ListPackagesInput(2, 0, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.plugins().size(), 2);
    }

    @Test
    void listPageSubset() {
        var input = new ListPackagesInput(2, 0, false);
        var result = smallInteractor.list(input);

        var correctIds = Set.of(1, 2);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.pageNumber(), 0);
        assertTrue(correctIds.stream().allMatch(
            id -> result.plugins().stream().anyMatch(plugin -> plugin.id() == id)
        ));
    }

    @Test
    void listNextPageSubset() {
        var input = new ListPackagesInput(2, 1, false);
        var result = smallInteractor.list(input);

        var correctIds = Set.of(3);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.pageNumber(), 1);
        assertTrue(correctIds.stream().allMatch(
            id -> result.plugins().stream().anyMatch(plugin -> plugin.id() == id)
        ));
    }

    @Test
    void listNextPageSubsetHasCorrectPluginCount() {
        var input = new ListPackagesInput(2, 1, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertEquals(result.plugins().size(), 1);
    }

    @Test
    void listNegativePageCount() {
        var input = new ListPackagesInput(2, -1, false);
        var result = smallInteractor.list(input);

        assertEquals(result.state(), ListPackagesResult.State.INVALID_INPUT);
    }

    @Test
    void listNegativeItemsPerPage() {
        var input = new ListPackagesInput(-1, 0, false);
        var result = smallInteractor.list(input);

        var correctIds = Set.of(1, 2, 3);

        assertEquals(result.state(), ListPackagesResult.State.SUCCESS);
        assertTrue(correctIds.stream().allMatch(
            id -> result.plugins().stream().anyMatch(plugin -> plugin.id() == id)
        ));
    }
}
