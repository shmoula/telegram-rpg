package cz.satorigeeks.telegramrpg.state

import eu.vendeli.tgbot.types.User
import java.util.concurrent.ConcurrentHashMap

object StateManager {
    private val stateByUser = ConcurrentHashMap<Long, GameState>()
    fun set(user: User, state: GameState) {
        stateByUser[user.id] = state
    }

    fun get(user: User) = stateByUser[user.id]
}