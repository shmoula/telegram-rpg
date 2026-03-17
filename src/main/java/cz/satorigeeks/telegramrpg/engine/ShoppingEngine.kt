package cz.satorigeeks.telegramrpg.engine

import cz.satorigeeks.telegramrpg.model.Hero
import cz.satorigeeks.telegramrpg.model.Item
import cz.satorigeeks.telegramrpg.model.ItemType

/**
 * Encapsulates shop logic: buying potions, checking gold and inventory space.
 */
class ShoppingEngine() {
    val shopItems = listOf(
        Item("Potion", ItemType.POTION, healingPower = 20, attackPower = 0, cost = 10),
        Item("Super Potion", ItemType.POTION, healingPower = 50, attackPower = 0, cost = 30),
        Item("Knife", ItemType.WEAPON, healingPower = 0, attackPower = 3, cost = 25),
        Item("Sword", ItemType.WEAPON, healingPower = 0, attackPower = 6, cost = 55),
        Item("Mace", ItemType.WEAPON, healingPower = 0, attackPower = 8, cost = 75)
    )

    /**
     * Attempts to purchase an item for the hero.
     * @param hero the player
     * @param item the item to purchase
     * @return a user-facing message describing the outcome
     */
    fun buy(hero: Hero, item: Item): String {
        return when {
            hero.money < item.cost -> "You do not have enough money to purchase that item."
            !hero.addToInventory(item.copy()) -> "You tried to purchase this but your inventory slots are full."
            else -> {
                hero.money -= item.cost
                "Purchased ${item.name}. Gold left: ${hero.money.toInt()}."
            }
        }
    }
}
