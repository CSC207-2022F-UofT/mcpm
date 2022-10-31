package org.hydev.mcpm;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

/**
 * Static collection of argument parsers for the command line
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class ArgsParser
{
    public static final ArgumentParser LOADER;

    static
    {
        LOADER = ArgumentParsers.newFor("loader").build().defaultHelp(true);
        LOADER.addArgument("name");
    }
}
