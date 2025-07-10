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

    private val inventory = MutableList<Item?>(5) { null }

    fun addToInventory(item: Item): Boolean {
        val idx = inventory.indexOfFirst { it == null }
        return if (idx >= 0) {
            inventory[idx] = item
            true
        } else false
    }

    fun useItem(slot: Int) {
        val item = inventory.getOrNull(slot - 1) ?: return
        health += item.healingPower
        inventory[slot - 1] = null

        println("You used ${item.name} and healed $name by ${item.healingPower} HP.")
        println("You have ${health.roundToInt()} HP left")
    }

    fun hasItem(slot: Int): Boolean {
        return inventory.getOrNull(slot - 1) != null
    }

    fun showInventory(): List<String> =
        inventory.mapIndexed { idx, item ->
            if (item != null) {
                "${idx + 1}) ${item.name} (heals ${item.healingPower})"
            } else {
                "${idx + 1}) [empty]"
            }
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
