package org.hydev.mcpm.client.commands;

import java.util.function.Consumer;

/**
 * Interface for a Command object.
 * <p>
 * For an example on creating a command, see the EchoEntry, EchoCommand and EchoParser classes.
 * <p>
 * Steps to create a new "Search" command:
 *   1. [A] Create a SearchEntry class.
 *      This class contains all your "input variable" for your command,
 *      So maybe add fields "searchMode," "searchText," "sortAlphabetically."
 *   2. [A] Create a SearchCommand class which implements Command&lt;SearchEntry&gt;.
 *      [B] Add a new "type" method: Class&lt;SearchEntry&gt; type() { return SearchEntry.class; }.
 *      This lets Java know what class your Command takes.
 *      [C] Add a new "run" method: void run(SearchEntry input, Consumer&lt;String&gt; log) { ... }.
 *      [D] Put anything your command does in this method,
 *      e.g. call SearchBoundary methods, collect all search results...
 *      To print anything to console, use the log parameter: log.accept("Hello World!");
 *      If your SearchCommand needs access to something like a SearchBoundary, create a constructor that takes it.
 *   3. [A] Create a SearchParser class which implements CommandParser.
 *      [B] Add a "configure" method: Subparser configure(Subparsers parsers) { }.
 *      This method describes what command line arguments your function takes.
 *      Call var parser = parsers.addParser("search") and make sure to return parser when you're done.
 *      For more info on taking arguments, take a look at <a href="https://argparse4j.github.io/usage.html">this</a>.
 *      [C] Add a "build" method: CommandEntry build(Namespace details) method.
 *      You should return a new SearchEntry from this class. Use details.getString, and other methods to initialize it.
 *   4. [A] Add your classes to CommandsFactory.
 *      [B] Go into the baseParsers() method and add new SearchParser() to the list.
 *      [C] Go into the baseCommands() method and add a CommandWrapper.wrap(new SearchCommand()) to the list.
 *      [D] If your SearchCommand takes a SearchBoundary as a parser, before the list, try initializing something like:
 *        var database = new DatabaseInteractor();
 *      And pass the database to your SearchCommand.
 *   5. You're done!
 *
 * @param <T> The type of the CommandEntry object.
 */
public interface Command<T extends CommandEntry> {
    /**
     * Return the command you are using.
     * If your command takes a PizzaEntry object, please just write `return PizzaEntry.class`.
     * This is needed to make your input class known at runtime.
     *
     * @return The runtime class of your input class T.
     */
    Class<T> type();

    /**
     * Called when your method is being run by your Controller.
     * Put all your "business logic" of your command here.
     *
     * @param input Your input parameter T.
     * @param log Use this instead of System.out.println.
     */
    void run(T input, Consumer<String> log);
}
