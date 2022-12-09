package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.arguments.parsers.*;
import org.hydev.mcpm.client.display.presenters.InstallPresenter;
import org.hydev.mcpm.client.display.presenters.KvInfoPresenter;
import org.hydev.mcpm.client.display.presenters.SearchPresenter;
import org.hydev.mcpm.client.display.presenters.UninstallPresenter;

import java.util.List;
import java.util.stream.Stream;

/**
 * Handles the creation of CommandParser list.
 * This is a simple factory that just invokes the methods of ControllerFactoryBoundary and returns a list of parsers.
 */
public class ParserFactory {
    private ParserFactory() { }

    /**
     * Creates a list of general parsers for the ArgsParser class.
     *
     * @return Returns a list of argument parsers that work in any environment (Server & CLI).
     */
    public static List<CommandParser> baseParsers(ControllerFactoryBoundary factory) {
        var searchPresenter = new SearchPresenter(factory.pageBoundary());
        var installPresenter = new InstallPresenter();
        var uninstallPresenter = new UninstallPresenter();
        var infoPresenter = new KvInfoPresenter();

        /*
         * Add general parsers to this list!
         * All versions of MCPM will have access to these parsers.
         * If you're not sure if your command is server-only, add it to this list!
         */
        return List.of(
            new ExportPluginsParser(factory.exportController()),
            new ImportParser(factory.importController()),
            new ListParser(factory.listController()),
            new SearchParser(factory.searchController(), searchPresenter),
            new MirrorParser(factory.mirrorController()),
            new InfoParser(factory.infoController(), infoPresenter),
            new InstallParser(factory.installController(), installPresenter),
            new RefreshParser(factory.refreshController()),
            new PageParser(factory.pageBoundary()),
            new UninstallParser(factory.uninstallController(), uninstallPresenter),
            new UpdateParser(factory.updateController())
        );
    }

    /**
     * Creates a list of server-only parsers for the ArgsParser class.
     *
     * @return Returns a list of argument parsers that require the Server (Minecraft Bukkit Plugin) environment.
     */
    public static List<CommandParser> serverParsers(ControllerFactoryBoundary factory) {
        /*
         * Add server-only parsers to this list!
         * Server only commands will not show up in the MCPM CLI.
         * They'll only be accessible when a user tries to execute a command from the MCPM Server Plugin (in-game).
         */
        var serverOnly = List.of(
            new LoadParser(factory.loadController()),
            new ReloadParser(factory.reloadController()),
            new UnloadParser(factory.unloadController())
        );

        return Stream.concat(baseParsers(factory).stream(), serverOnly.stream()).toList();
    }

}
