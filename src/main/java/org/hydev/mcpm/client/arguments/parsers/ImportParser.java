package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.util.function.Consumer;

/**
 * Parser for the import use case
 */
public class ImportParser implements CommandParser {
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

    }

    @Override
    public void run(Namespace details, Consumer<String> log) {

    }
}
