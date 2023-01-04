package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Subparser

/**
 * Provides an interface for providing Command details and setting up a Subparser object.
 */
interface CommandConfigurator
{
    /**
     * Name of the command
     *
     * @return Name
     */
    val name: String

    /**
     * Description of the command. If the description is empty, it will not show up in the help menu.
     *
     * @return Description
     */
    val description: String

    /**
     * Usually, the body of this command goes as follows:
     *
     * <pre>
     * parser.addArgument("searchParameter").dest("searchText");
     * parser.addArgument("searchType").dest("searchType");
    </pre> *
     *
     * See [usage](https://argparse4j.github.io/usage.html) for how to add arguments.
     *
     *
     * This will ensure that your build method gets called when your subcommand is executed.
     * Just do it!
     *
     * @param parser A subparser
     */
    fun configure(parser: Subparser)
}
