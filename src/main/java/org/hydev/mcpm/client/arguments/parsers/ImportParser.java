package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.ImportController;
import org.hydev.mcpm.client.export.ImportInput;

import java.util.function.Consumer;

/**
 * Parser for the import use case
 */
public record ImportParser(ImportController controller) implements CommandParser {
    @Override
    public String name() {
        return "import";
    }

    @Override
    public String description() {
        return "Import a plugins config from a previous export";
    }

    @Override
    public void configure(Subparser parser) {
        parser.addArgument("type").nargs("?").choices("pastebin", "file", "literal")
                .setDefault("pastebin") // type of input
                .type(String.class).dest("type"); // of type OutputStream
        parser.addArgument("input").nargs("?")
                .type(String.class).dest("input");
    }

    @Override
    public void run(Namespace details, Consumer<String> log) {
        controller.importPlugins(new ImportInput(details.get("type"), details.get("input")), log);
    }
}
