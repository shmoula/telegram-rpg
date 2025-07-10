package cz.satorigeeks.telegramrpg.engine

import cz.satorigeeks.telegramrpg.model.Hero
import cz.satorigeeks.telegramrpg.model.Item

/**
 * Encapsulates shop logic: buying potions, checking gold and inventory space.
 */
class ShoppingEngine() {
    val shopItems = listOf(
        Item("Potion", 20, 10),
        Item("Super Potion", 50, 30)
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
            !hero.addToInventory(item) -> "You tried to purchase this but your inventory slots are full."
            else -> {
                hero.money -= item.cost
                "Purchased ${item.name}. Gold left: ${hero.money.toInt()}."
            }
        }
    }
}
