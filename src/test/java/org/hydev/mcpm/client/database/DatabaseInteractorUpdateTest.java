package org.hydev.mcpm.client.database;

import java.util.HashSet;
import java.util.List;
import java.util.OptionalLong;

import org.hydev.mcpm.client.database.inputs.CheckForUpdatesInput;
import org.hydev.mcpm.client.database.inputs.CheckForUpdatesResult;
import org.hydev.mcpm.client.database.model.PluginModelId;
import org.hydev.mcpm.client.database.model.PluginVersionId;
import org.hydev.mcpm.client.database.model.PluginVersionState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseInteractorUpdateTest {
    static DatabaseInteractor emptyInteractor;
    static DatabaseInteractor smallInteractor;

    @BeforeAll
    static void setup() {
        emptyInteractor = PluginMockFactory.interactor(List.of());

        smallInteractor = PluginMockFactory.interactor(List.of(
            PluginMockFactory.model(1),
            PluginMockFactory.model(2, "name", List.of("ver.1", "ver.2")),
            PluginMockFactory.model(3, "test", List.of("update", "update update")),
            PluginMockFactory.model(4, "test", List.of("1.0", "2.0", "3.0"))
        ));
    }

    @Test
    void updateUpToDateByVersionId() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byId(2));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().isEmpty());
        assertTrue(result.mismatched().isEmpty());
    }

    @Test
    void updateUpToDateByVersionString() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byString("3.0"));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().isEmpty());
        assertTrue(result.mismatched().isEmpty());
    }

    @Test
    void updateByVersionId() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byId(1));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().containsKey(id));
        assertEquals(result.updatable().get(id).id(), 4);
        assertTrue(result.mismatched().isEmpty());
    }

    @Test
    void updateByVersionString() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byString("2.0"));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().containsKey(id));
        assertEquals(result.updatable().get(id).id(), 4);
        assertTrue(result.mismatched().isEmpty());
    }

    @Test
    void updateMixed() {
        var latest = new PluginVersionState(PluginModelId.byId(2), PluginVersionId.byString("ver.2"));
        var outdated = new PluginVersionState(PluginModelId.byId(3), PluginVersionId.byString("update"));
        var mismatched = new PluginVersionState(PluginModelId.byId(7), PluginVersionId.byId(2));

        var input = new CheckForUpdatesInput(List.of(latest, outdated, mismatched), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertEquals(result.updatable().size(), 1);
        assertEquals(result.updatable().get(outdated).id(), 3);
        assertEquals(result.mismatched().size(), 1);
        assertEquals(result.mismatched().get(0), mismatched);
    }

    @Test
    void updateEmptyVersions() {
        var id = new PluginVersionState(PluginModelId.byId(1), PluginVersionId.byString("ver.2"));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().isEmpty());
        assertTrue(result.mismatched().isEmpty());
    }

    @Test
    void updateEmptyDatabase() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byId(1));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = emptyInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().isEmpty());
        assertEquals(result.mismatched().size(), 1);
        assertTrue(result.mismatched().contains(id));
    }

    @Test
    void updateInvalidPluginId() {
        var invalid = new PluginModelId(OptionalLong.empty(), null, null);
        var id = new PluginVersionState(invalid, PluginVersionId.byId(1));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = emptyInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.INVALID_INPUT);
        assertTrue(result.updatable().isEmpty());
        assertTrue(result.mismatched().isEmpty());
    }

    @Test
    void updateInvalidVersionId() {
        var invalid = new PluginVersionId(OptionalLong.empty(), null);
        var id = new PluginVersionState(PluginModelId.byId(3), invalid);

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = emptyInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.INVALID_INPUT);
    }

    @Test
    void updateMismatched() {
        var id = new PluginVersionState(PluginModelId.byId(7), PluginVersionId.byId(1));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.mismatched().contains(id));
        assertEquals(result.mismatched().size(), 1);
        assertTrue(result.updatable().isEmpty());
    }
}
