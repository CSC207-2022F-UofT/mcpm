package org.hydev.mcpm.client.local;

import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.GeneralUtils;
import org.hydev.mcpm.utils.TemporaryDir;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for SuperLocalPluginTracker
 */
@Tag("IntegrationTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SuperLocalPluginTrackerTest
{
    private static final String TESTING_PLUGIN = "TestingPlugin";
    
    static TemporaryDir tmp;
    static LocalPluginTracker tracker;

    @BeforeAll
    static void init()
    {
        tmp = new TemporaryDir();
        var lock = new File(tmp.path, "lock.json");
        var f = Objects.requireNonNull(GeneralUtils.getResourceFile("test-plugin-activelist.jar"));
        tracker = new LocalPluginTracker(lock.getAbsolutePath(), f.getParentFile().getAbsolutePath());
    }

    @AfterAll
    static void destroy()
    {
        tmp.close();
    }

    @Test
    @Order(1)
    void addEntry()
    {
        tracker.addEntry(TESTING_PLUGIN, true, 101, 591);

        assertTrue(tracker.listEntries().stream().anyMatch(it -> it.getName().equals(TESTING_PLUGIN)));
    }

    @Test
    @Order(2)
    void listManuallyInstalled()
    {
        assertTrue(tracker.listManuallyInstalled().contains(TESTING_PLUGIN));
        assertTrue(tracker.listManuallyInstalled().contains("ActiveList"));
    }

    @Test
    @Order(3)
    void removeEntry()
    {
        tracker.removeEntry(TESTING_PLUGIN);

        assertTrue(tracker.listManuallyInstalled().contains(TESTING_PLUGIN));
        assertFalse(tracker.listEntries().stream().anyMatch(it -> it.getName().equals(TESTING_PLUGIN)));
    }

    @Test
    @Order(4)
    void listOrphanPlugins2()
    {
        var orphans = tracker.listOrphanPlugins(false).stream().map(PluginYml::name).toList();

        assertFalse(orphans.contains(TESTING_PLUGIN));
        assertFalse(orphans.contains("ActiveList"));
    }

    @Test
    void listInstalled()
    {
        var installed = tracker.listInstalled();
        assertTrue(installed.stream().anyMatch(it -> it.name().equals("ActiveList")));
        assertTrue(installed.stream().anyMatch(it -> it.name().equals(TESTING_PLUGIN)));
    }
}
