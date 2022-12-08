package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.SearchPackagesController;
import org.hydev.mcpm.client.commands.presenters.SearchResultPresenter;

import java.util.List;
import java.util.function.Consumer;

/**
 * SearchParser has two arguments: "type" and "text."
 * When the user runs the search command, the program prompts the user to specify the type of
 * search and the search text.
 */
public record SearchParser(SearchPackagesController controller,
                           SearchResultPresenter presenter) implements CommandParser {
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
        // Default search by name, if -k is specified then search by keyword
        var type = parser.addMutuallyExclusiveGroup();
        type.addArgument("-k", "--keyword").action(Arguments.storeTrue()).dest("byKeyword")
            .help("Search by keyword in descriptions");
        type.addArgument("-c", "--command").action(Arguments.storeTrue()).dest("byCommand")
            .help("Search by command");

        // Content of the search
        parser.addArgument("text").dest("text").nargs("+")
            .help("Specifies the search text.");
        parser.addArgument("-n", "--no-cache").action(Arguments.storeTrue()).dest("noCache")
            .help("Specifies whether to use local cache or not.");
    }

    @Override
    public void run(Namespace details, Consumer<String> log) {
        // Convert type
        var type = "name";
        if (details.getBoolean("byKeyword"))
            type = "keyword";
        if (details.getBoolean("byCommand"))
            type = "command";

        // Call searchPackages
        List<String> t = details.getList("text");

        var result = controller.searchPackages(type, t, details.getBoolean("noCache"));
        presenter.displayResult(result, log);
    }
}
