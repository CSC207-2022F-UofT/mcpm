package org.hydev.mcpm.client.interaction

/**
 * An interface to interact with the user.
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-02
 */
interface ILogger
{
    /**
     * Ask the user for an input (non-blocking with coroutine)
     *
     * @return Text of the user's input
     */
    suspend fun input(): String

    /**
     * Ask the user for an input with prompt
     */
    suspend fun input(prompt: String): String
    {
        print(prompt)
        return input()
    }

    /**
     * Output something (Colored)
     *
     * @param txt: Text to be printed
     */
    fun print(txt: String)
}
