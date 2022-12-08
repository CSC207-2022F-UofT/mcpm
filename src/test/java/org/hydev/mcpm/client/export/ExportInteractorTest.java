package org.hydev.mcpm.client.export;

import org.hydev.mcpm.client.commands.controllers.ExportController;
import org.hydev.mcpm.client.database.PluginMockFactory;
import org.hydev.mcpm.client.database.tracker.MockPluginTracker;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ExportInteractorTest {
    static ExportInteractor interactor;
    static PluginTracker pt;

    @BeforeAll
    static void init()
    {
        var plugins = new ArrayList<>(PluginMockFactory.generateTestPlugins());
        plugins.remove(0); // remove first plugin with no version

        pt = new MockPluginTracker(plugins);

        interactor = new ExportInteractor(pt);
    }

    @Test
    @Tag("IntegrationTest")
    void testExportImport() {
        var exportController = new ExportController(interactor, (exportPluginsResult, log) -> {});
        exportController.export(new ExportPluginsInput("literal", ""), System.out::println);
    }
}