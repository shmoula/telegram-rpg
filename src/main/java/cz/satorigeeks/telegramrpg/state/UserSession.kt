package cz.satorigeeks.telegramrpg.state

import cz.satorigeeks.telegramrpg.model.Enemy
import cz.satorigeeks.telegramrpg.model.Hero

data class UserSession(
    val hero: Hero,
    var gameState: GameState,
    var enemy: Enemy? = null,
    var heroFirst: Boolean = true
)