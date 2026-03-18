package cz.satorigeeks.telegramrpg.db

import org.jetbrains.exposed.v1.core.Table

object Users : Table("users") {
    val id = long("tg_id")
    val name = varchar("name", 50)
    val health = double("health")
    val attackPower = double("attack_power")
    val level = integer("level")
    val experience = integer("experience")
    val money = float("money")
    val inventory = text("inventory")

    override val primaryKey = PrimaryKey(id)
}
