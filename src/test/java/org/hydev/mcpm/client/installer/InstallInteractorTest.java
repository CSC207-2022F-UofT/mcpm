//package org.hydev.mcpm.client.installer;
//
//import org.hydev.mcpm.client.database.fetcher.QuietFetcherListener;
//import org.hydev.mcpm.client.installer.input.InstallInput;
//import org.hydev.mcpm.client.installer.output.InstallResult;
//import org.hydev.mcpm.client.local.LocalDatabaseFetcher;
//import org.hydev.mcpm.client.models.PluginYml;
//import org.hydev.mcpm.client.search.SearchInteractor;
//import org.hydev.mcpm.client.search.SearchPackagesType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Tests the `install` interactor classes for correct behaviour.
// */
//public class InstallInteractorTest {
//    private Installer installer;
//    private MockLocalPluginTracker mockLocalTracker;
//
//    /**
//     * Test Setup: Create an empty directory before testing
//     */
//    @BeforeEach
//    public void setup() {
//        var fetcher = new LocalDatabaseFetcher(() -> URI.create("https://mcpm.hydev.org"));
//        mockLocalTracker = new MockLocalPluginTracker();
//        installer = new Installer(
//            new MockDownloader(),
//            null,
//            new SearchInteractor(fetcher, new QuietFetcherListener()),
//            mockLocalTracker
//        );
//    }
//
//
//    /**
//     * Test case: Successfully installed plugin
//     */
//    @Test
//    public void testPluginInstalledSuccess() {
//
//        InstallInput installInput = new InstallInput("PandaPL",
//                                                            SearchPackagesType.BY_NAME,
//                                                            false,
//                                                            true);
//        List<InstallResult> listInstallResult = installer.installPlugin(installInput);
//
//        InstallResult pandaplInstallResult = listInstallResult.get(0);
//        assertEquals(pandaplInstallResult.name(), "PandaPL");
//        assertEquals(pandaplInstallResult.type(), InstallResult.Type.SUCCESS_INSTALLED);
//
//        // Test behaviour: file exists after installing
//        List<String> listFileInstalled = new ArrayList<>();
//        List<PluginYml> pluginYmlInstalled = mockLocalTracker.listInstalled();
//        pluginYmlInstalled.forEach(pluginYml -> listFileInstalled.add(pluginYml.name()));
//        assertEquals(pluginYmlInstalled.size(), 1);
//        assertTrue(listFileInstalled.contains(installInput.name()));
//    }
//
//    /**
//     * Test case: User wants load the plugin
//     */
//    @Test
//    public void testPluginInstalledUnLoad() {
//
//        InstallInput installInput = new InstallInput("PandaPL",
//                                                            SearchPackagesType.BY_NAME,
//                                                            false,
//                                                            true);
//        List<InstallResult> listInstallResult = installer.installPlugin(installInput);
//
//        InstallResult pandaplLoadResult = listInstallResult.get(0);
//        assertEquals(pandaplLoadResult.name(), "PandaPL");
//        assertFalse(pandaplLoadResult.loaded());
//    }
//
//    /**
//     * Test case: User doesn't want to load the plugin
//     * Noted: This test would be failed loaded because the loader can only be instantiated
//     *        when the minecraft server is running
//     */
//    @Test
//    public void testPluginInstalledLoad_But_Unloaded() {
//        InstallInput installInput = new InstallInput("PandaPL",
//                                                     SearchPackagesType.BY_NAME,
//                                                    true,
//                                                    true);
//        List<InstallResult> listInstallResult = installer.installPlugin(installInput);
//        InstallResult pandaplLoadResult = listInstallResult.get(0);
//        assertEquals(pandaplLoadResult.name(), "PandaPL");
//        assertFalse(pandaplLoadResult.loaded());
//    }
//
//    /**
//     * Test case: invalid input, this happens when input name is empty -> searchPackageresult is null
//     */
//    @Test
//    public void testInvalidInput() {
//        InstallInput installInput = new InstallInput("",
//                SearchPackagesType.BY_NAME,
//                false,
//                true);
//        List<InstallResult> listInstallResult = installer.installPlugin(installInput);
//        InstallResult installResult = listInstallResult.get(0);
//        assertEquals(installResult.name(), "");
//        assert (installResult.type() == InstallResult.Type.SEARCH_INVALID_INPUT);
//    }
//
//    /**
//     * Test case: plugin not found, this happends when input name is rando and does
//     *             not match with database
//     */
//    @Test
//    public void testSearchPluginNotFound() {
//        InstallInput installInput = new InstallInput("UwU",
//                                                        SearchPackagesType.BY_NAME,
//                                                        false,
//                                                        true);
//        List<InstallResult> listInstallResult = installer.installPlugin(installInput);
//        InstallResult installResult = listInstallResult.get(0);
//        assertEquals(installResult.name(), "UwU");
//        assertEquals(installResult.type(), InstallResult.Type.NOT_FOUND);
//    }
//
//    /**
//     * Test case: plugin shouldn't be installed if it already exists locally
//     */
//    @Test
//    public void testPluginAlreadyInstalledLocally() {
//        InstallInput installInput = new InstallInput("JedCore",
//                SearchPackagesType.BY_NAME,
//                false,
//                true);
//        installer.installPlugin(installInput);
//        var listInstallResult = installer.installPlugin(installInput);
//
//        InstallResult installResult = listInstallResult.get(0);
//        assertEquals(installResult.name(), "ProjectKorra");
//        assertTrue(mockLocalTracker.findIfInLockByName("ProjectKorra"));
//        assertEquals(installResult.type(), InstallResult.Type.PLUGIN_EXISTS);
//    }
//
//    /**
//     * Test case: Successfully install the plugin with the dependency
//     */
//    @Test
//    public void test_InstallDependency_OfInputPlugin() {
//        InstallInput installInput = new InstallInput("JedCore",
//                                                            SearchPackagesType.BY_NAME,
//                                                            false,
//                                                            true);
//        List<InstallResult> listInstallResult = installer.installPlugin(installInput);
//        assertEquals(listInstallResult.size(), 2);
//
//        InstallResult jedcoreInstallResult = listInstallResult.get(1);
//        assertEquals(jedcoreInstallResult.name(), "JedCore");
//        assertEquals(jedcoreInstallResult.type(),  InstallResult.Type.SUCCESS_INSTALLED);
//
//        InstallResult korraInstallResult = listInstallResult.get(0);
//        assertEquals(korraInstallResult.name(), "ProjectKorra");
//        assertEquals(korraInstallResult.type(), InstallResult.Type.SUCCESS_INSTALLED);
//
//        assertEquals(mockLocalTracker.listInstalled().size(), 2);
//    }
//
//    /**
//     * Test case: Installing the (uninstalled) dependency should still go through
//     * if the input plugin has already been installed
//     */
//    @Test
//    public void test_InstallMissingDependency_OfInputPlugin() {
//        InstallInput installInput = new InstallInput("JedCore",
//                                                        SearchPackagesType.BY_NAME,
//                                                        false,
//                                                        true);
//        installer.installPlugin(installInput);
//        // Assume the user doesn't like ProjectKorra dependency, and he/she/they uninstalled it
//        mockLocalTracker.removeEntry("ProjectKorra");
//        assertEquals(mockLocalTracker.listInstalled().size(), 1);
//
//        List<InstallResult> listInstallResult = installer.installPlugin(installInput);
//        // Ensure that ProjectKorra is installed and each plugin has 2 result states
//        assertEquals(listInstallResult.size(), 2);
//        assertEquals(mockLocalTracker.listInstalled().size(), 2);
//
//        InstallResult jedCoreInstallResult = listInstallResult.get(1);
//        assertEquals(jedCoreInstallResult.name(), "JedCore");
//        assertEquals(jedCoreInstallResult.type(),  InstallResult.Type.PLUGIN_EXISTS);
//
//        InstallResult projectKorraInstallResult = listInstallResult.get(0);
//        assertEquals(projectKorraInstallResult.name(), "ProjectKorra");
//        assertEquals(projectKorraInstallResult.type(), InstallResult.Type.SUCCESS_INSTALLED);
//    }
//}
