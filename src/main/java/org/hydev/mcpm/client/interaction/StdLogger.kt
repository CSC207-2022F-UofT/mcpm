package org.hydev.mcpm.client.interaction

import org.hydev.mcpm.utils.ColorLogger.printc

/**
 * Take input and give output using STD IO
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-02
 */
class StdLogger : ILogger
{
    override suspend fun input(): String?
    {
        // Since in the CLI mode, there are only ever going to have one user, so blocking is fine
        return readlnOrNull()
    }

    override fun print(txt: String)
    {
        printc(txt)
    }
}
