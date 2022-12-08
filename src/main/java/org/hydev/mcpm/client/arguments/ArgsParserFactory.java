package org.hydev.mcpm.client.arguments;

/**
 * Class that produces default implementations of the Controller and ArgsParser classes.
 * These classes are used to handle commands.
 * Checkout Command.java for a process on adding a new command!
 */
public class ArgsParserFactory {
    // No instantiation.
    private ArgsParserFactory() { }

    /**
     * Creates an ArgsParser object that has general parsers (works in any environment).
     *
     * @return An ArgsParser object. Invoke ArgsParser#parse to see more.
     */
    public static ArgsParser baseArgsParser() {
        var interactors = new InteractorFactory(false);
        var controllers = new ControllerFactory(interactors);

        return new ArgsParser(ParserFactory.baseParsers(controllers));
    }

    /**
     * Creates an ArgsParser object that has all (CLI & Server) parsers.
     *
     * @return An ArgsParser object. Invoke ArgsParser#parse to see more.
     */
    public static ArgsParser serverArgsParser() {
        var interactors = new InteractorFactory(true);
        var controllers = new ControllerFactory(interactors);

        return new ArgsParser(ParserFactory.serverParsers(controllers));
    }
}
