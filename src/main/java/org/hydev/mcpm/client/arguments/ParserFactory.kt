package org.hydev.mcpm.client.arguments

import org.hydev.mcpm.client.arguments.parsers.*
import org.hydev.mcpm.client.display.presenters.InstallPresenter
import org.hydev.mcpm.client.display.presenters.KvInfoPresenter
import org.hydev.mcpm.client.display.presenters.SearchPresenter
import org.hydev.mcpm.client.display.presenters.UninstallPresenter
import java.util.stream.Stream

/**
 * Handles the creation of CommandParser list.
 * This is a simple factory that just invokes the methods of ControllerFactoryBoundary and returns a list of parsers.
 */
object ParserFactory
{
    /**
     * Creates a list of general parsers for the ArgsParser class.
     *
     * @return Returns a list of argument parsers that work in any environment (Server & CLI).
     */
    fun baseParsers(factory: IControllerFactory): List<CommandParser>
    {
        val searchPresenter = SearchPresenter(factory.pageBoundary())
        val installPresenter = InstallPresenter()
        val uninstallPresenter = UninstallPresenter()
        val infoPresenter = KvInfoPresenter()

        /*
         * Add general parsers to this list!
         * All versions of MCPM will have access to these parsers.
         * If you're not sure if your command is server-only, add it to this list!
         */
        return listOf(
            ExportPluginsParser(factory.exportController()),
            ImportParser(factory.importController()),
            ListParser(factory.listController()),
            SearchParser(factory.searchController(), searchPresenter),
            MirrorParser(factory.mirrorController()),
            InfoParser(factory.infoController(), infoPresenter),
            InstallParser(factory.installController(), installPresenter),
            RefreshParser(factory.refreshController()),
            PageParser(factory.pageBoundary()),
            UninstallParser(factory.uninstallController(), uninstallPresenter),
            UpdateParser(factory.updateController()),
            CatParser()
        )
    }

    /**
     * Creates a list of server-only parsers for the ArgsParser class.
     *
     * @return Returns a list of argument parsers that require the Server (Minecraft Bukkit Plugin) environment.
     */
    fun serverParsers(factory: IControllerFactory): List<CommandParser>
    {
        /*
         * Add server-only parsers to this list!
         * Server only commands will not show up in the MCPM CLI.
         * They'll only be accessible when a user tries to execute a command from the MCPM Server Plugin (in-game).
         */
        val serverOnly = listOf(
            LoadParser(factory.loadController()),
            ReloadParser(factory.reloadController()),
            UnloadParser(factory.unloadController())
        )
        return Stream.concat(baseParsers(factory).stream(), serverOnly.stream()).toList()
    }
}
