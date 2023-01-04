package org.hydev.mcpm.client.arguments

/**
 * Class that produces default implementations of the Controller and ArgsParser classes.
 * These classes are used to handle commands.
 * Checkout Command.java for a process on adding a new command!
 */
object ArgsParserFactory
{
    /**
     * Creates an ArgsParser object that has general parsers (works in any environment).
     *
     * @return An ArgsParser object. Invoke ArgsParser#parse to see more.
     */
    fun baseArgsParser(): ArgsParser
    {
        val interactors = InteractorFactory(false)
        val controllers = ControllerFactory(interactors)
        return ArgsParser(ParserFactory.baseParsers(controllers))
    }

    /**
     * Creates an ArgsParser object that has all (CLI & Server) parsers.
     *
     * @return An ArgsParser object. Invoke ArgsParser#parse to see more.
     */
    fun serverArgsParser(): ArgsParser
    {
        val interactors = InteractorFactory(true)
        val controllers = ControllerFactory(interactors)
        return ArgsParser(ParserFactory.serverParsers(controllers))
    }
}
