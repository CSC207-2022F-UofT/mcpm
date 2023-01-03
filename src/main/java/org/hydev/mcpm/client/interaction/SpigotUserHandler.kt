package org.hydev.mcpm.client.interaction

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Take in user input through Spigot
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-02
 */
class SpigotUserHandler
{
    // Listening[uuid] = handler
    private val listening: HashMap<UUID, (String) -> Unit> = HashMap()

    /**
     * When player say something, if we're listening, we hijack the event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onSay(e: AsyncPlayerChatEvent) = listening[e.player.uniqueId]?.let {
        // Run callback
        it(e.message)

        // Prevent further event handlers from processing
        e.isCancelled = true
    }

    /**
     * When player quit, we stop listening to them.
     */
    @EventHandler
    fun onQuit(e: PlayerQuitEvent) = listening.remove(e.player.uniqueId)

    /**
     * Create a user interactor for a player
     */
    fun create(p: Player) = object : ILogger
    {
        override suspend fun input() = suspendCoroutine { async -> listening[p.uniqueId] = { async.resume(it) } }

        override fun print(txt: String) = p.sendMessage(txt.replace("&", "§"))
    }
}
