package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparser;

import java.util.Map;

/**
 * Print help argument action to create help messages
 */
public record PrintHelpAction(Subparser subparser) implements ArgumentAction
{
    @Override
    public void run(ArgumentParser parser, Argument arg, Map<String, Object> attrs,
                    String flag, Object value) throws HelpException
    {
        throw new HelpException(parser, subparser.formatHelp());
    }

    @Override
    public void onAttach(Argument arg)
    {
    }

    @Override
    public boolean consumeArgument()
    {
        return false;
    }
}
