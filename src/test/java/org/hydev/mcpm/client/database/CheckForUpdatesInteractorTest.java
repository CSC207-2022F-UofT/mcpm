package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.matcher.MatchPluginsInteractor;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.database.fetcher.ConstantFetcher;
import org.hydev.mcpm.client.database.fetcher.QuietFetcherListener;
import org.hydev.mcpm.client.updater.CheckForUpdatesInput;
import org.hydev.mcpm.client.matcher.PluginModelId;
import org.hydev.mcpm.client.matcher.PluginVersionId;
import org.hydev.mcpm.client.matcher.PluginVersionState;
import org.hydev.mcpm.client.updater.CheckForUpdatesInteractor;
import org.hydev.mcpm.client.updater.CheckForUpdatesResult;
import org.hydev.mcpm.client.models.PluginModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.OptionalLong;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the default CheckForUpdatesInteractor's update method for correct behaviour.
 */
public class CheckForUpdatesInteractorTest {
    private static CheckForUpdatesBoundary emptyInteractor;
    private static CheckForUpdatesBoundary smallInteractor;

    static CheckForUpdatesBoundary interactor(List<PluginModel> plugins) {
        var fetcher = new ConstantFetcher(plugins);
        var listener = new QuietFetcherListener();

        // Best to mock this too, but these tests were tied to this originally.
        var matcher = new MatchPluginsInteractor(fetcher, listener);

        return new CheckForUpdatesInteractor(matcher);
    }

    /**
     * Create relevant interactors for tests.
     */
    @BeforeAll
    public static void setup() {
        emptyInteractor = interactor(List.of());

        smallInteractor = interactor(List.of(
            PluginMockFactory.model(1),
            PluginMockFactory.model(2, "name", null, List.of("ver.1", "ver.2"), null),
            PluginMockFactory.model(3, "test", null, List.of("update", "update update"), null),
            PluginMockFactory.model(4, "test", null, List.of("1.0", "2.0", "3.0"), null)
        ));
    }

    /**
     * Test the update method with an up-to-date version id.
     */
    @Test
    public void updateUpToDateByVersionId() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byId(2));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().isEmpty());
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the update method with an up-to-date version string.
     */
    @Test
    public void updateUpToDateByVersionString() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byString("3.0"));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().isEmpty());
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the update method with an outdated version id.
     */
    @Test
    public void updateByVersionId() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byId(1));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().containsKey(id));
        assertEquals(result.updatable().get(id).id(), 4);
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the update method with an outdated version string.
     */
    @Test
    public void updateByVersionString() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byString("2.0"));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().containsKey(id));
        assertEquals(result.updatable().get(id).id(), 4);
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the update method with a mixture of latest, outdated and mismatched version identifiers.
     */
    @Test
    public void updateMixed() {
        var latest = new PluginVersionState(PluginModelId.byId(2), PluginVersionId.byString("ver.2"));
        var outdated = new PluginVersionState(PluginModelId.byId(3), PluginVersionId.byString("update"));
        var mismatched = new PluginVersionState(PluginModelId.byId(7), PluginVersionId.byId(2));

        var input = new CheckForUpdatesInput(List.of(latest, outdated, mismatched), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertEquals(result.updatable().size(), 1);
        assertEquals(result.updatable().get(outdated).id(), 3);
        assertEquals(result.mismatched(), Set.of(mismatched));
    }

    /**
     * Test the update method with an empty set of versions.
     */
    @Test
    public void updateEmptyVersions() {
        var id = new PluginVersionState(PluginModelId.byId(1), PluginVersionId.byString("ver.2"));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().isEmpty());
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the update method with an empty database.
     */
    @Test
    public void updateEmptyDatabase() {
        var id = new PluginVersionState(PluginModelId.byId(4), PluginVersionId.byId(1));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = emptyInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.updatable().isEmpty());
        assertEquals(result.mismatched().size(), 1);
        assertTrue(result.mismatched().contains(id));
    }

    /**
     * Test the update method with an invalid plugin id (returns INVALID_INPUT).
     */
    @Test
    public void updateInvalidPluginId() {
        var invalid = new PluginModelId(OptionalLong.empty(), null, null);
        var id = new PluginVersionState(invalid, PluginVersionId.byId(1));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = emptyInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.INVALID_INPUT);
        assertTrue(result.updatable().isEmpty());
        assertTrue(result.mismatched().isEmpty());
    }

    /**
     * Test the update method with an invalid version id (returns INVALID_INPUT).
     */
    @Test
    public void updateInvalidVersionId() {
        var invalid = new PluginVersionId(OptionalLong.empty(), null);
        var id = new PluginVersionState(PluginModelId.byId(3), invalid);

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = emptyInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.INVALID_INPUT);
    }

    /**
     * Test the update method with a mismatched (plugin that doesn't exist).
     */
    @Test
    public void updateMismatched() {
        var id = new PluginVersionState(PluginModelId.byId(7), PluginVersionId.byId(1));

        var input = new CheckForUpdatesInput(List.of(id), false);
        var result = smallInteractor.updates(input);

        assertEquals(result.state(), CheckForUpdatesResult.State.SUCCESS);
        assertTrue(result.mismatched().contains(id));
        assertEquals(result.mismatched().size(), 1);
        assertTrue(result.updatable().isEmpty());
    }
}
