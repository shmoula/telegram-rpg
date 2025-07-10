package cz.satorigeeks.telegramrpg.model

import kotlin.random.Random

open class Character(
    val name: String,
    var health: Double,
    var attackPower: Double
) {
    val isAlive: Boolean
        get() = health > 0

    fun runAway(): Boolean =
        Random.nextBoolean()
}
