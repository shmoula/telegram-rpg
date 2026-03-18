package cz.satorigeeks.telegramrpg.db

import cz.satorigeeks.telegramrpg.model.Hero
import cz.satorigeeks.telegramrpg.model.Item
import cz.satorigeeks.telegramrpg.model.ItemType
import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert

object UserRepository {
    fun save(user: User, hero: Hero) {
        transaction {
            val inventoryText = serializeInventory(hero.getInventory())
            Users.upsert {
                it[id] = user.id
                it[name] = hero.name
                it[health] = hero.health
                it[attackPower] = hero.attackPower
                it[level] = hero.level
                it[experience] = hero.experience
                it[money] = hero.money
                it[inventory] = inventoryText
            }
        }
    }

    fun delete(user: User) {
        transaction {
            Users.deleteWhere { id eq user.id }
        }
    }

    fun load(user: User): Hero? =
        transaction {
            val row = Users.selectAll()
                .where { Users.id eq user.id }
                .limit(1)
                .firstOrNull()
                ?: return@transaction null

            val inventory = deserializeInventory(row[Users.inventory])
            Hero.fromPersistence(
                name = row[Users.name],
                health = row[Users.health],
                attackPower = row[Users.attackPower],
                level = row[Users.level],
                experience = row[Users.experience],
                money = row[Users.money],
                inventory = inventory
            )
        }

    private fun serializeInventory(items: List<Item?>): String =
        json.encodeToString(items.map { item ->
            item?.let {
                InventoryItem(
                    name = it.name,
                    type = it.type.name,
                    healingPower = it.healingPower,
                    attackPower = it.attackPower,
                    cost = it.cost,
                    isEquipped = it.isEquipped
                )
            }
        })

    private fun deserializeInventory(text: String): List<Item?> {
        if (text.isBlank()) {
            return List(Hero.MAX_INVENTORY_CAPACITY) { null }
        }
        val decoded = runCatching { json.decodeFromString<List<InventoryItem?>>(text) }
            .getOrElse { return List(Hero.MAX_INVENTORY_CAPACITY) { null } }

        val mapped = decoded.map { item ->
            item?.let {
                val type = runCatching { ItemType.valueOf(it.type) }.getOrNull() ?: return@let null
                Item(
                    name = it.name,
                    type = type,
                    healingPower = it.healingPower,
                    attackPower = it.attackPower,
                    cost = it.cost,
                    isEquipped = it.isEquipped
                )
            }
        }

        return when {
            mapped.size < Hero.MAX_INVENTORY_CAPACITY ->
                mapped + List(Hero.MAX_INVENTORY_CAPACITY - mapped.size) { null }

            mapped.size > Hero.MAX_INVENTORY_CAPACITY ->
                mapped.take(Hero.MAX_INVENTORY_CAPACITY)

            else -> mapped
        }
    }

    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    @Serializable
    private data class InventoryItem(
        val name: String,
        val type: String,
        val healingPower: Int,
        val attackPower: Int,
        val cost: Int,
        val isEquipped: Boolean
    )
}
