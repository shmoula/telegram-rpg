package cz.satorigeeks.telegramrpg.state

import cz.satorigeeks.telegramrpg.model.Hero

data class UserSession(
    val hero: Hero,
    var gameState: GameState
)