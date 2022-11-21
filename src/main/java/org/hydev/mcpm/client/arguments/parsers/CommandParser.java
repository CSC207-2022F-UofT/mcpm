package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Implemented by parsers that define methods to "configure" ArgsParse4j and then "build" a CommandEntry object.
 */
public interface CommandParser {
    /**
     * Name of the command
     *
     * @return Name
     */
    String name();

    /**
     * Usually, the body of this command goes as follows:
     *
     * <pre>
     *   parser.addArgument("searchParameter").dest("searchText");
     *   parser.addArgument("searchType").dest("searchType");
     * </pre>
     *
     * See <a href="https://argparse4j.github.io/usage.html">usage</a> for how to add arguments.
     * <p>
     * This will ensure that your build method gets called when your subcommand is executed.
     * Just do it!
     *
     * @param parser A subparser
     */
    void configure(Subparser parser);

    /**
     * Called when the user executed the Subparser command in configure.
     * The details of what the user entered ("searchText", "searchType") are in the details object.
     * We should return a CommandEntry object (e.g. SearchEntry) that contains the parameters that the user expects.
     *
     * <pre>
     *   return new SearchEntry(
     *     details.getString("searchText"),
     *     details.getString("searchType")
     *   );
     * </pre>
     *
     * @param details A details object that contains all the arguments that the user executed this command with.
     */
    void run(Namespace details, Consumer<String> log);
}
