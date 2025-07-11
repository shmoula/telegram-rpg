package cz.satorigeeks.telegramrpg.model

class Enemy(
    name: String,
    health: Double,
    attackPower: Double,
    var magicPower: Int
) : Character(name, health, attackPower) {
    fun copy(): Enemy {
        return Enemy(name, health, attackPower, magicPower)
    }
}
