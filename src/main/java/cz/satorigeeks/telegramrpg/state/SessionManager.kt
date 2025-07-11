package cz.satorigeeks.telegramrpg.state

import cz.satorigeeks.telegramrpg.model.Enemy
import cz.satorigeeks.telegramrpg.model.Hero
import eu.vendeli.tgbot.types.User

object SessionManager {
    private val sessions = mutableMapOf<Long, UserSession>()

    fun get(user: User): UserSession =
        sessions.getOrPut(user.id) {
            UserSession(Hero(user.firstName, 100.0, 10.0), GameState.MAIN_MENU)
        }

    fun setState(user: User, newState: GameState) {
        get(user).gameState = newState
    }

    fun getState(user: User): GameState {
        return get(user).gameState
    }

    fun getHero(user: User): Hero {
        return get(user).hero
    }

    fun setEnemy(user: User, enemy: Enemy) {
        get(user).enemy = enemy
    }

    fun getEnemy(user: User): Enemy? {
        return get(user).enemy
    }

    fun setHeroFirst(user: User, heroFirst: Boolean) {
        get(user).heroFirst = heroFirst
    }

    fun  getHeroFirst(user: User): Boolean {
        return get(user).heroFirst
    }
}
