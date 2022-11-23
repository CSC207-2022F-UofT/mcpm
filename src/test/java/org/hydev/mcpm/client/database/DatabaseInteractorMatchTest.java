package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.inputs.MatchPluginsInput;
import org.hydev.mcpm.client.database.inputs.MatchPluginsResult;
import org.hydev.mcpm.client.database.model.PluginModelId;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.OptionalLong;

/**
 * Tests the DatabaseInteractor's match method.
 */
public class DatabaseInteractorMatchTest {
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
     * Test the match command for an empty search (search for nothing).
     */
    @Test
    void matchEmptyInput() {
        var input = new MatchPluginsInput(List.of(), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.SUCCESS);
        assertTrue(result.mismatched().isEmpty());
        assertTrue(result.matched().isEmpty());
    }

    /**
     * Test the match command for an PluginModelId object.
     */
    @Test
    void matchInvalidIdentifier() {
        var input = new MatchPluginsInput(List.of(
            new PluginModelId(OptionalLong.empty(), null, null)
        ), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.INVALID_INPUT);
    }

    /**
     * Test the match command to find one plugin by plugin id.
     */
    @Test
    void matchOneById() {
        var id = PluginModelId.byId(2);

        var input = new MatchPluginsInput(List.of(id), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.SUCCESS);
        assertTrue(result.matched().containsKey(id));
        assertTrue((result.matched().get(id).id() == 2));
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the match command to find many plugins by plugin id.
     */
    @Test
    void matchManyById() {
        var first = PluginModelId.byId(2);
        var second = PluginModelId.byId(3);

        var input = new MatchPluginsInput(List.of(first, second), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.SUCCESS);
        assertTrue(result.matched().containsKey(first));
        assertTrue(result.matched().containsKey(second));
        assertTrue((result.matched().get(first).id() == 2));
        assertTrue((result.matched().get(second).id() == 3));
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the match command to properly classify unknown plugins as mismatched.
     */
    @Test
    void matchMismatchedOnly() {
        var id = PluginModelId.byId(6);

        var input = new MatchPluginsInput(List.of(id), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.SUCCESS);
        assertTrue(result.matched().isEmpty());
        assertTrue(result.mismatched().contains(id));
        assertEquals(result.mismatched().size(), 1);
    }

    /**
     * Test the match command to properly sort many plugins into matched/mismatched.
     */
    @Test
    void matchMismatchedAndMatched() {
        var first = PluginModelId.byId(2);
        var second = PluginModelId.byId(6);

        var input = new MatchPluginsInput(List.of(first, second), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.SUCCESS);
        assertTrue(result.matched().containsKey(first));
        assertTrue((result.matched().get(first).id() == 2));
        assertTrue(result.mismatched().contains(second));
        assertEquals(result.mismatched().size(), 1);
    }

    /**
     * Test the match command to properly find plugins by plugin name.
     */
    @Test
    void matchByName() {
        var id = PluginModelId.byName("hello");

        var input = new MatchPluginsInput(List.of(id), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.SUCCESS);
        assertTrue(result.matched().containsKey(id));
        assertEquals(result.matched().get(id).id(), 2);
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the match command to properly find plugins by plugin main class.
     */
    @Test
    void matchByMain() {
        var id = PluginModelId.byMain("org.hello"); // Mock Generates main by prefixing org.

        var input = new MatchPluginsInput(List.of(id), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.SUCCESS);
        assertTrue(result.matched().containsKey(id));
        assertEquals(result.matched().get(id).id(), 2);
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the match command to properly return nothing from an empty database.
     */
    @Test
    void matchEmptyDatabase() {
        var id = PluginModelId.byId(6);

        var input = new MatchPluginsInput(List.of(id), false);
        var result = emptyInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.SUCCESS);
        assertTrue(result.matched().isEmpty());
        assertTrue(result.mismatched().contains(id));
        assertEquals(result.mismatched().size(), 1);
    }
}
