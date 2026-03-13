package cz.satorigeeks.telegramrpg.model

import kotlin.math.roundToInt

class Hero(
    name: String,
    health: Double,
    attackPower: Double
) : Character(name, health, attackPower) {

    companion object {
        const val MAX_INVENTORY_CAPACITY = 5
    }

    var level: Int = 1
        private set

    var experience: Int = 0
        set(value) {
            field = value
            checkLevelUp()
        }

    var money: Float = 50f

    private val inventory = MutableList<Item?>(MAX_INVENTORY_CAPACITY) { null }
    private var pendingLevelUpMessage: String? = null

    fun addToInventory(item: Item?): Boolean {
        val emptySlot = inventory.indexOfFirst { it == null }
        return if (emptySlot >= 0) {
            inventory[emptySlot] = item
            true
        } else {
            false
        }
    }

    fun useItem(item: Item) {
        health += item.healingPower
    }

    fun removeFromInventory(slot: Int) {
        inventory[slot] = null
    }

    fun showInventory(): List<String> =
        inventory.mapIndexed { idx, item ->
            if (item != null) {
                "${idx + 1}) ${item.name} (heals ${item.healingPower})"
            } else {
                "${idx + 1}) empty"
            }
        }

    fun getInventory(): List<Item?> =
        inventory

    fun isInventoryEmpty(): Boolean {
        return inventory.all { it == null }
    }

    private fun checkLevelUp() {
        if (experience >= 100) {
            level++
            experience -= 100
            attackPower *= 1.1
            health *= 1.1

            pendingLevelUpMessage = "Congrats! You leveled up to level $level"
        }
    }

    fun consumeLevelUpMessage(): String? {
        val message = pendingLevelUpMessage
        pendingLevelUpMessage = null
        return message
    }

    fun getInfo(): String {
        return "\uD83D\uDC64 Hero name: " + name + "\n" +
                "❤\uFE0F Hero health: " + health.roundToInt() + "\n" +
                "⚔\uFE0F Hero attack power: " + attackPower.roundToInt() + "\n" +
                "\uD83D\uDCC8 Hero level: " + level + "\n" +
                "✨ Hero experience: " + experience + "\n" +
                "\uD83D\uDCB0 Hero bank account: " + money + "\n\n" +
                "*Inventory:* \n" + showInventory().joinToString(separator = "\n")
    }
}
