package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.inputs.MatchPluginsInput;
import org.hydev.mcpm.client.database.inputs.MatchPluginsResult;
import org.hydev.mcpm.client.database.model.PluginModelId;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.OptionalLong;

public class DatabaseInteractorMatchTest {
    static DatabaseInteractor emptyInteractor;
    static DatabaseInteractor smallInteractor;

    @BeforeAll
    static void setup() {
        emptyInteractor = PluginMockFactory.interactor(List.of());

        smallInteractor = PluginMockFactory.interactor(List.of(
            PluginMockFactory.model(1),
            PluginMockFactory.model(2, "hello"),
            PluginMockFactory.model(3, "test")
        ));
    }

    @Test
    void matchEmptyInput() {
        var input = new MatchPluginsInput(List.of(), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.SUCCESS);
        assertTrue(result.mismatched().isEmpty());
        assertTrue(result.matched().isEmpty());
    }

    @Test
    void matchInvalidIdentifier() {
        var input = new MatchPluginsInput(List.of(
            new PluginModelId(OptionalLong.empty(), null, null)
        ), false);
        var result = smallInteractor.match(input);

        assertEquals(result.state(), MatchPluginsResult.State.INVALID_INPUT);
    }

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
