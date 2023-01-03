package org.hydev.mcpm.client.interaction

/**
 * An interface to interact with the user.
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-02
 */
interface IUserInteractor
{
    /**
     * Ask the user for an input (non-blocking with coroutine)
     *
     * @return Text of the user's input
     */
    suspend fun input(): String?

    /**
     * Output something (Colored)
     *
     * @param txt: Text to be printed
     */
    fun print(txt: String)
}
