package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import org.hydev.mcpm.client.commands.controllers.UpdateController;
import org.hydev.mcpm.client.display.presenters.LogUpdatePresenter;
import org.hydev.mcpm.client.installer.presenter.InstallPresenter;
import org.hydev.mcpm.client.installer.presenter.InstallResultPresenter;

import java.util.function.Consumer;

/**
 * Handles parsing related to the update command.
 *
 * @param controller A controller to dispatch an update command when invoked.
 */
public record UpdateParser(UpdateController controller) implements CommandParser {
    @Override
    public String name() {
        return "update";
    }

    @Override
    public String description() {
        return "Updates plugins to the latest version.";
    }

    @Override
    public void configure(Subparser parser) {
        // if (Constants.IS_MINECRAFT) {
        parser.addArgument("--load")
            .type(boolean.class)
            .action(Arguments.storeTrue())
            .dest("load")
            .help("If true, updated plugins will be reloaded after the update.");
        // }

        parser.addArgument("--no-cache")
            .type(boolean.class)
            .action(Arguments.storeTrue())
            .dest("no-cache")
            .help("If true, the cache will be skipped and database will be fetched again.");

        parser.addArgument("names")
            .nargs("*")
            .help("List of plugin names to update.");
    }

    @Override
    public void run(Namespace details, Consumer<String> log) {
        // Since log can change from invocation to invocation,
        // and I don't want UpdatePresenter to depend on Consumer<String>,
        // I'll instantiate this every call.
        var presenter = new LogUpdatePresenter(log);
        InstallResultPresenter installPresenter = new InstallPresenter(log);

        controller.update(
            details.getList("names"),
            details.getBoolean("load"),
            details.getBoolean("no-cache"),
            presenter,
            installPresenter
        );
    }
}
