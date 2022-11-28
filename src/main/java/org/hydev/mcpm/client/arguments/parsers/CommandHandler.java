package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;

import java.util.function.Consumer;

/**
 * Provides an implementation for a Command.
 * Typically, an implementation will involve grabbing view details from the Namespace and calling a controller.
 */
public interface CommandHandler
{
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
