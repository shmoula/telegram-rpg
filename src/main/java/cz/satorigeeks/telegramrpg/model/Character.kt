package cz.satorigeeks.telegramrpg.model

import kotlin.math.max
import kotlin.random.Random

open class Character(
    val name: String,
    health: Double,
    var attackPower: Double
) {
    var health: Double = max(0.0, health)
        set(value) {
            field = max(0.0, value)
        }

    val isAlive: Boolean
        get() = health > 0

    fun runAway(): Boolean =
        Random.nextBoolean()
}
