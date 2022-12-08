package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.fetcher.ConstantFetcher;
import org.hydev.mcpm.client.database.fetcher.QuietFetcherListener;
import org.hydev.mcpm.client.installer.output.InstallResult;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test methods related to the Update plugins use case.
 * To accomplish this, we use mock installers, plugin trackers and databases.
 */
public class UpdateInteractorTest {
    private record MockPackage(
        UpdateBoundary updator,
        MockPluginTracker pluginTracker,
        MockInstaller mockInstaller
    ) { }

    /**
     * Create an interactor with provided parameters.
     *
     * @param plugins A repository of all plugins. Will be passed to database unless emptyDatabase is true.
     * @param installedVersions Maps from plugin id to versions of plugins that are "installed" in the mock tracker.
     * @param installerSucceeds Determines the return value of the mock installer (false for failure).
     * @param emptyDatabase If true, regardless of the value of plugins, the database will contain no plugins.
     * @return A MockPackage object, consisting of an UpdateBoundary, PluginTracker and Installer.
     */
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

        var defaultResult = installerSucceeds
                ? InstallResult.Type.SUCCESS_INSTALLED
                : InstallResult.Type.SEARCH_FAILED_TO_FETCH_DATABASE;

        var tracker = new MockPluginTracker(installed);
        var installer = new MockInstaller(plugins, tracker, defaultResult);

        var interactor = new UpdateInteractor(checker, installer, tracker);

        return new MockPackage(
            interactor,
            tracker,
            installer
        );
    }

    /**
     * Default plugins in a database for utility.
     *
     * @return A list of mock PluginModel objects.
     */
    private static List<PluginModel> defaultPlugins() {
        return List.of(
            PluginMockFactory.model(1, "TpProtect", List.of("0.0.0.0.1", "0.0.3", "9.99")),
            PluginMockFactory.model(2, "JedCore", List.of("version-1", "version-2", "version-3")),
            PluginMockFactory.model(3, "Apples+", List.of("1.0", "1.2")),
            PluginMockFactory.model(4, "TpProtect", List.of("0.0.0.0.1", "0.0.3", "9.99"))
        );
    }

    /**
     * Returns an interactor with an empty database and empty plugin tracker.
     *
     * @return A MockPackage object.
     */
    private static MockPackage emptyInteractor() {
        return interactor(List.of(), Map.of(), true, false);
    }

    /**
     * Returns an interactor with an empty database but one installed plugin (JedCore).
     *
     * @return A MockPackage object.
     */
    private static MockPackage emptyWithInstalledInteractor() {
        return interactor(
            defaultPlugins(),
            Map.of(2L, "version-2"),
            true, true
        );
    }

    /**
     * Returns an interactor with all installed plugins marked as up-to-date.
     *
     * @return A MockPackage object.
     */
    private static MockPackage upToDateInteractor() {
        return interactor(defaultPlugins(), Map.of(
            2L, "version-3",
            3L, "1.2",
            4L, "9.99"
        ), true, false);
    }

    /**
     * Returns an interactor with one installed plugin marked as out-of-date.
     *
     * @return A MockPackage object.
     */
    private static MockPackage oneOldInteractor() {
        return interactor(defaultPlugins(), Map.of(
            2L, "version-2",
            3L, "1.2",
            4L, "9.99"
        ), true, false);
    }

    /**
     * Returns an interactor with one out-of-date plugin, with a strictly failing mock installer.
     *
     * @return A MockPackage object.
     */
    private static MockPackage failingOneOldInteractor() {
        return interactor(defaultPlugins(), Map.of(
            2L, "version-2",
            3L, "1.2",
            4L, "9.99"
        ), false, false);
    }

    /**
     * Returns an interactor with all installed plugins marked as out-of-date.
     *
     * @return A MockPackage object.
     */
    private static MockPackage outOfDateInteractor() {
        return interactor(defaultPlugins(), Map.of(
            2L, "version-2",
            3L, "1.0",
            4L, "0.0.3"
        ), true, false);
    }

    /**
     * Asserts if the outcome is not UP_TO_DATE.
     */
    private static void assertUpToDate(UpdateOutcome outcome, String version) {
        assertEquals(outcome.state(), UpdateOutcome.State.UP_TO_DATE);
        assertEquals(outcome.initialVersion(), version);
        assertNull(outcome.destinationVersion());
    }

    /**
     * Asserts if the outcome is not UPDATED.
     */
    private static void assertUpdated(UpdateOutcome outcome, String version, String destination) {
        assertEquals(outcome.state(), UpdateOutcome.State.UPDATED);
        assertEquals(outcome.initialVersion(), version);
        assertEquals(outcome.destinationVersion(), destination);
    }

    /**
     * Tests that update does nothing on an empty database/plugin tracker.
     */
    @Test
    void testEmptyDatabase() {
        var mock = emptyInteractor();

        var input = new UpdateInput(List.of(), false, false);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertTrue(result.outcomes().isEmpty());
    }

    /**
     * Tests that update correctly marks a plugin that does not exist in the database as MISMATCHED.
     */
    @Test
    void testMismatchedPlugin() {
        var mock = emptyWithInstalledInteractor();

        var input = new UpdateInput(List.of("JedCore"), false, false);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertEquals(result.outcomes().get("JedCore").state(), UpdateOutcome.State.MISMATCHED);
    }

    /**
     * Tests that update correctly marks a given plugin that is not in the plugin tracker as NOT_INSTALLED.
     */
    @Test
    void testMismatchedName() {
        var mock = emptyInteractor();

        var input = new UpdateInput(List.of("JedCore"), false, false);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertEquals(result.outcomes().get("JedCore").state(), UpdateOutcome.State.NOT_INSTALLED);
    }

    /**
     * Tests that update correctly marks all up-to-date plugins as UP_TO_DATE.
     */
    @Test
    void testAllUpToDate() {
        var mock = upToDateInteractor();

        var input = new UpdateInput(List.of(), false, false);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertEquals(result.outcomes().size(), 3);
        assertUpToDate(result.outcomes().get("JedCore"), "version-3");
        assertUpToDate(result.outcomes().get("Apples+"), "1.2");
        assertUpToDate(result.outcomes().get("TpProtect"), "9.99");
    }

    /**
     * Tests that update correctly marks a specific given plugin as UP_TO_DATE.
     */
    @Test
    void testOneUpToDate() {
        var mock = upToDateInteractor();

        var input = new UpdateInput(List.of("Apples+"), false, false);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertTrue(mock.mockInstaller().getRequested().isEmpty());
        assertEquals(result.outcomes().size(), 1);
        assertUpToDate(result.outcomes().get("Apples+"), "1.2");
    }

    /**
     * Tests that update correctly marks all installed plugins as OUT_OF_DATE.
     */
    @Test
    void testAllOutOfDate() {
        var mock = outOfDateInteractor();

        var input = new UpdateInput(List.of(), false, false);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertEquals(mock.mockInstaller().getRequested(), Set.of("JedCore", "Apples+", "TpProtect"));
        assertEquals(result.outcomes().size(), 3);
        assertUpdated(result.outcomes().get("JedCore"), "version-2", "version-3");
        assertUpdated(result.outcomes().get("Apples+"), "1.0", "1.2");
        assertUpdated(result.outcomes().get("TpProtect"), "0.0.3", "9.99");
    }

    /**
     * Tests that update correctly marks a given plugin as OUT_OF_DATE.
     */
    @Test
    void testOneOutOfDate() {
        var mock = outOfDateInteractor();

        var input = new UpdateInput(List.of("Apples+"), false, false);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertEquals(mock.mockInstaller().getRequested(), Set.of("Apples+"));
        assertEquals(result.outcomes().size(), 1);
        assertUpdated(result.outcomes().get("Apples+"), "1.0", "1.2");
    }

    /**
     * Tests that update correctly marks a mixture of OUT_OF_DATE and UP_TO_DATE plugins.
     */
    @Test
    void testAllMixed() {
        var mock = oneOldInteractor();

        var input = new UpdateInput(List.of(), false, false);
        //assertNotNull(mock.updator);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertEquals(mock.mockInstaller().getRequested(), Set.of("JedCore"));
        assertEquals(result.outcomes().size(), 3);
        assertUpdated(result.outcomes().get("JedCore"), "version-2", "version-3");
        assertUpToDate(result.outcomes().get("Apples+"), "1.2");
        assertUpToDate(result.outcomes().get("TpProtect"), "9.99");
    }

    /**
     * Tests that update correctly conveys a NETWORK_ERROR on an installation failure.
     */
    @Test
    void testFailingInstaller() {
        var mock = failingOneOldInteractor();

        var input = new UpdateInput(List.of(), false, false);
        var result = mock.updator.update(input);

        assertEquals(result.state(), UpdateResult.State.SUCCESS);
        assertEquals(result.outcomes().size(), 3);
        assertEquals(result.outcomes().get("JedCore").state(), UpdateOutcome.State.NETWORK_ERROR);
        assertFalse(result.outcomes().get("JedCore").state().success());
        assertUpToDate(result.outcomes().get("Apples+"), "1.2");
        assertUpToDate(result.outcomes().get("TpProtect"), "9.99");
    }
}
