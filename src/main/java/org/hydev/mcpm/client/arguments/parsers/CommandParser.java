package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Subparser;

/**
 * Implemented by parsers that define methods to "configure" ArgsParse4j and then "build" a CommandEntry object.
 */
public interface CommandParser extends CommandHandler {
    /**
     * Name of the command
     *
     * @return Name
     */
    String name();

    /**
     * Description of the command
     *
     * @return Description
     */
    String description();

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
}
