package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.UninstallController;
import org.hydev.mcpm.client.uninstall.UninstallResult;

import java.util.function.Consumer;

/**
 * Command parser for the uninstallation use case
 */
public record UninstallParser(UninstallController controller) implements CommandParser {
    @Override
    public String name() {
        return "uninstall";
    }

    @Override
    public String description() {
        return "Uninstall a plugin from file system";
    }

    @Override
    public void configure(Subparser parser) {
        parser.addArgument("name")
            .help("Name of the plugin to uninstall");

        parser.addArgument("-n", "--no-recursive")
            .action(Arguments.storeFalse())
            .setDefault(true)
            .dest("recursive")
            .help("Recursively remove orphan dependencies");
    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        var name = details.getString("name");

        // Uninstall
        var result = controller.uninstall(name, details.getBoolean("recursive"));

        // Print result
        if (result.state() == UninstallResult.State.FAILED_TO_DELETE) {
            log.accept("&cFailed to delete plugin file");
        }
        if (result.state() == UninstallResult.State.SUCCESS) {
            log.accept("&aPlugin " + name + " uninstalled successfully!");
        }
    }
}
