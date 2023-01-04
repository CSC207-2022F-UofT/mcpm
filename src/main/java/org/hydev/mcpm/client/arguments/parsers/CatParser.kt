package org.hydev.mcpm.client.arguments.parsers

import kotlinx.coroutines.*
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.interaction.ILogger

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-02
 */
class CatParser : CommandParser
{
    override val name = "cat"
    override val description = "Echo what you type in the prompt until you exit"

    override suspend fun run(details: Namespace, log: ILogger)
    {
        log.print("&bWelcome to 🐱. I'll repeat what you say until you type exit.")
        while (true)
        {
            val a = log.input()
            if (a == "exit") return
            log.print("🐱: $a")
        }
    }

    override fun configure(parser: Subparser) {}
}
