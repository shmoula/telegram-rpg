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

    fun addToInventory(item: Item?): Boolean {
        if (inventory.size < maxCapacity) {
            inventory.add(item)
            return true
        } else
            return false
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
            experience = 0
            attackPower *= 1.1
            health *= 1.1

            println("Congrats! You leveled up to level $level");
        }
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
