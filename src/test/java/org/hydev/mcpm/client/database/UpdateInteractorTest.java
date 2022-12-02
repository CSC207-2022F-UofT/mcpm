package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;
import org.hydev.mcpm.client.database.fetcher.ConstantFetcher;
import org.hydev.mcpm.client.database.fetcher.QuietFetcherListener;
import org.hydev.mcpm.client.matcher.MatchPluginsInteractor;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.updater.CheckForUpdatesInteractor;
import org.hydev.mcpm.client.updater.UpdateBoundary;
import org.hydev.mcpm.client.updater.UpdateInput;
import org.hydev.mcpm.client.updater.UpdateInteractor;
import org.hydev.mcpm.client.updater.UpdateOutcome;
import org.hydev.mcpm.client.updater.UpdateResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateInteractorTest {
    private static final InstallResultPresenter installPresenter = new SilentInstallPresenter();

    private record MockPackage(
        UpdateBoundary updator,
        MockPluginTracker pluginTracker,
        MockInstaller mockInstaller
    ) { }

    private static MockPackage interactor(
        List<PluginModel> plugins,
        Map<Long, String> installedVersions,
        boolean installerSucceeds,
        boolean emptyDatabase
    ) {
        var fetcher = new ConstantFetcher(emptyDatabase ? List.of() : plugins);
        var listener = new QuietFetcherListener();

        // Best to mock this too, but these tests were tied to this originally.
        var matcher = new MatchPluginsInteractor(fetcher, listener);
        var checker = new CheckForUpdatesInteractor(matcher);

        var installed = plugins.stream()
            .filter(plugin -> installedVersions.containsKey(plugin.id()))
            .map(plugin -> plugin.versions().stream()
                .filter(version -> installedVersions.get(plugin.id()).equals(version.meta().version()))
                .findFirst())
            .filter(Optional::isPresent)
            .map(version -> version.get().meta())
            .toList();

        var tracker = new MockPluginTracker(installed);
        var installer = new MockInstaller(plugins, tracker, installerSucceeds);

        var interactor = new UpdateInteractor(checker, installer, tracker);

        return new MockPackage(
            interactor,
            tracker,
            installer
        );
    }

    private static List<PluginModel> defaultPlugins() {
        return List.of(
            PluginMockFactory.model(1, "TpProtect", List.of("0.0.0.0.1", "0.0.3", "9.99")),
            PluginMockFactory.model(2, "JedCore", List.of("version-1", "version-2", "version-3")),
            PluginMockFactory.model(3, "Apples+", List.of("1.0", "1.2")),
            PluginMockFactory.model(4, "TpProtect", List.of("0.0.0.0.1", "0.0.3", "9.99"))
        );
    }

    private static MockPackage emptyInteractor() {
        return interactor(List.of(), Map.of(), true, false);
    }

    private static MockPackage emptyWithInstalledInteractor() {
        return interactor(
            defaultPlugins(),
            Map.of(2L, "version-2"),
            true, true
        );
    }

    private static MockPackage upToDateInteractor() {
        return interactor(defaultPlugins(), Map.of(
            2L, "version-3",
            3L, "1.2",
            4L, "9.99"
        ), true, false);
    }

    private static MockPackage oneOldInteractor() {
        return interactor(defaultPlugins(), Map.of(
            2L, "version-2",
            3L, "1.2",
            4L, "9.99"
        ), true, false);
    }

    private static MockPackage failingOneOldInteractor() {
        return interactor(defaultPlugins(), Map.of(
            2L, "version-2",
            3L, "1.2",
            4L, "9.99"
        ), false, false);
    }

    private static MockPackage outOfDateInteractor() {
        return interactor(defaultPlugins(), Map.of(
            2L, "version-2",
            3L, "1.0",
            4L, "0.0.3"
        ), true, false);
    }

    private static void assertUpToDate(UpdateOutcome outcome, String version) {
        assertEquals(outcome.state(), UpdateOutcome.State.UP_TO_DATE);
        assertEquals(outcome.initialVersion(), version);
        assertNull(outcome.destinationVersion());
    }

    private static void assertUpdated(UpdateOutcome outcome, String version, String destination) {
        assertEquals(outcome.state(), UpdateOutcome.State.UPDATED);
        assertEquals(outcome.initialVersion(), version);
        assertEquals(outcome.destinationVersion(), destination);
    }

    @Test
    void testEmptyDatabase() {
        var mock = emptyInteractor();

        var input = new UpdateInput(List.of(), false, false, installPresenter);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertTrue(result.outcomes().isEmpty());
    }

    @Test
    void testMismatchedPlugin() {
        var mock = emptyWithInstalledInteractor();

        var input = new UpdateInput(List.of("JedCore"), false, false, installPresenter);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertEquals(result.outcomes().get("JedCore").state(), UpdateOutcome.State.MISMATCHED);
    }

    @Test
    void testMismatchedName() {
        var mock = emptyInteractor();

        var input = new UpdateInput(List.of("JedCore"), false, false, installPresenter);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertEquals(result.outcomes().get("JedCore").state(), UpdateOutcome.State.NOT_INSTALLED);
    }

    @Test
    void testAllUpToDate() {
        var mock = upToDateInteractor();

        var input = new UpdateInput(List.of(), false, false, installPresenter);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertEquals(result.outcomes().size(), 3);
        assertUpToDate(result.outcomes().get("JedCore"), "version-3");
        assertUpToDate(result.outcomes().get("Apples+"), "1.2");
        assertUpToDate(result.outcomes().get("TpProtect"), "9.99");
    }

    @Test
    void testOneUpToDate() {
        var mock = upToDateInteractor();

        var input = new UpdateInput(List.of("Apples+"), false, false, installPresenter);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertEquals(result.outcomes().size(), 1);
        assertUpToDate(result.outcomes().get("Apples+"), "1.2");
    }

    @Test
    void testAllOutOfDate() {
        var mock = outOfDateInteractor();

        var input = new UpdateInput(List.of(), false, false, installPresenter);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertEquals(mock.mockInstaller().getRequested(), Set.of("JedCore", "Apples+", "TpProtect"));
        assertEquals(result.outcomes().size(), 3);
        assertUpdated(result.outcomes().get("JedCore"), "version-2", "version-3");
        assertUpdated(result.outcomes().get("Apples+"), "1.0", "1.2");
        assertUpdated(result.outcomes().get("TpProtect"), "0.0.3", "9.99");
    }

    @Test
    void testOneOutOfDate() {
        var mock = outOfDateInteractor();

        var input = new UpdateInput(List.of("Apples+"), false, false, installPresenter);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertEquals(mock.mockInstaller().getRequested(), Set.of("Apples+"));
        assertEquals(result.outcomes().size(), 1);
        assertUpdated(result.outcomes().get("Apples+"), "1.0", "1.2");
    }

    @Test
    void testAllMixed() {
        var mock = oneOldInteractor();

        var input = new UpdateInput(List.of(), false, false, installPresenter);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertEquals(mock.mockInstaller().getRequested(), Set.of("JedCore"));
        assertEquals(result.outcomes().size(), 3);
        assertUpdated(result.outcomes().get("JedCore"), "version-2", "version-3");
        assertUpToDate(result.outcomes().get("Apples+"), "1.2");
        assertUpToDate(result.outcomes().get("TpProtect"), "9.99");
    }

    @Test
    void testFailingInstaller() {
        var mock = failingOneOldInteractor();

        var input = new UpdateInput(List.of(), false, false, installPresenter);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertEquals(result.outcomes().size(), 3);
        assertEquals(result.outcomes().get("JedCore").state(), UpdateOutcome.State.NETWORK_ERROR);
        assertFalse(result.outcomes().get("JedCore").state().success());
        assertUpToDate(result.outcomes().get("Apples+"), "1.2");
        assertUpToDate(result.outcomes().get("TpProtect"), "9.99");
    }
}
