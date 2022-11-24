package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.entries.InstallController;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.installer.InstallException;

import java.util.function.Consumer;

public record InstallParser(InstallController controller) implements CommandParser
{
    @Override
    public String name() {
        return "install";
    }

    @Override
    public String description() {
        return "Download and install a plugin from the database";
    }

    @Override
    public void configure(Subparser parser) {
        parser.addArgument("name").dest("name")
                .help("The name of the plugin you want to install");
        parser.addArgument("--no-load").action(Arguments.storeTrue()).dest("noLoad")
                .help("Default load, use this option if you don't want to load after install");
    }

    @Override
    public void run(Namespace details, Consumer<String> log) throws InstallException {
        var name = details.getString("name");
        controller.install(name, SearchPackagesType.BY_NAME, !details.getBoolean("noLoad"), log);
    }
}
