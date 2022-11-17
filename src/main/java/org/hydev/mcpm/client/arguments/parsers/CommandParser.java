package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.CommandEntry;
import org.jetbrains.annotations.Nullable;

/**
 * Implemented by parsers that define methods to "configure" ArgsParse4j and then "build" a CommandEntry object.
 */
public interface CommandParser {
    /**
     * Usually, the body of this command goes as follows:
     *
     * <pre>
     *   var parser = parsers.addParser("search");
     *
     *   parser.addArgument("searchParameter").dest("searchText");
     *   parser.addArgument("searchType").dest("searchType");
     *
     *   return parser;
     * </pre>
     *
     * See <a href="https://argparse4j.github.io/usage.html">usage</a> for how to add arguments.
     * <p>
     * Return the Subparser you created object when possible!
     * This will ensure that your build method gets called when your subcommand is executed.
     * Just do it!
     *
     * @param parsers A subparsers
     * @return A Subparser that will be configured so that if this command is executed, the build method will be called.
     */
    @Nullable
    Subparser configure(Subparsers parsers);

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
     * @return A command entry object containing the details that the user is trying to execute.
     */
    CommandEntry build(Namespace details);
}
