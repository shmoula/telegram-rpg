package cz.satorigeeks.telegramrpg.model

import kotlin.math.roundToInt

class Hero(
    name: String,
    health: Double,
    attackPower: Double
) : Character(name, health, attackPower) {

    var level: Int = 1
        private set

    var experience: Int = 0
        set(value) {
            field = value
            checkLevelUp()
        }

    var money: Float = 50f

    private val maxCapacity = 5
    private val inventory = mutableListOf<Item?>()
    private var pendingLevelUpMessage: String? = null

    fun addToInventory(item: Item?): Boolean {
        val emptySlot = inventory.indexOfFirst { it == null }
        return when {
            emptySlot >= 0 -> {
                inventory[emptySlot] = item
                true
            }

            inventory.size < maxCapacity -> {
                inventory.add(item)
                true
            }

            else -> false
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
                "${idx + 1}) [empty]"
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
        return "Hero name: " + name + "\n" +
                "Hero health: " + health.roundToInt() + "\n" +
                "Hero attack power: " + attackPower.roundToInt() + "\n" +
                "Hero level: " + level + "\n" +
                "Hero experience: " + experience + "\n" +
                "Hero bank account: " + money + "\n" +
                "Inventory: " + showInventory()
    }
}
