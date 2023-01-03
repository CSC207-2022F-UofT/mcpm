package org.hydev.mcpm.client.interaction

/**
 * A logger that does nothing
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-02
 */
class NullLogger : ILogger
{
    override suspend fun input(): String = ""
    override fun print(txt: String) {}
}
