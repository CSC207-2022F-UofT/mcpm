package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.entries.SearchPackagesController;
import java.util.function.Consumer;

/**
 * SearchParser has two arguments: "type" and "text."
 * When the user runs the search command, the program prompts the user to specify the type of
 * search and the search text.
 */
public class SearchParser implements CommandParser {
    private final SearchPackagesController controller;

    public SearchParser(SearchPackagesController controller) {
        this.controller = controller;
    }

    @Override
    public String name() {
        return "search";
    }

    @Override
    public String description() {
        return "Search for a plugin in the database";
    }

    @Override
    public void configure(Subparser parser) {
        parser.addArgument("by").choices("name", "keyword", "command").dest("type")
                .help("Specifies the Type of Search");
        parser.addArgument("text").dest("text").nargs("+")
                .help("Specifies the search text.");
        parser.addArgument("-n", "--no-cache").action(Arguments.storeTrue()).dest("noCache")
                .help("Specifies whether to use local cache or not.");
    }

    @Override
    public void run(Namespace details, Consumer<String> log) {
        controller.searchPackages("BY_" + details.getString("type").toUpperCase(),
                String.join(" ", details.getList("text")),
                !details.getBoolean("noCache"), log);
    }
}
