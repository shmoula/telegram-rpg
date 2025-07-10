package cz.satorigeeks.telegramrpg.model

class Enemy(
    name: String,
    health: Double,
    attackPower: Double,
    var magicPower: Int
) : Character(name, health, attackPower)
