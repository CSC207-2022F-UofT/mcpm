package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.entries.EchoController;

import java.util.function.Consumer;

/**
 * Demo parser object. EchoParser has one argument "text." When the user runs the echo command, the "text" argument that
 * the user entered is passed through to the build method, which returns an EchoEntry object!
 * <p>
 * The EchoEntry object is then passed to Controller, which will pass it to EchoCommand (since EchoCommand extends
 * Command&lt;EchoEntry&gt;).
 */
public class EchoParser implements CommandParser
{
    private final EchoController controller;

    public EchoParser(EchoController controller)
    {
        this.controller = controller;
    }

    @Override
    public String name()
    {
        return "echo";
    }

    @Override
    public String description()
    {
        return "Testing command to echo what you say";
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("text").dest("text")
            .help("Text to echo");
    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        controller.echo(details.getString("text"), log);
    }
}
