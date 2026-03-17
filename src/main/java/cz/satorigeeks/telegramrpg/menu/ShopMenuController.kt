package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.engine.ShoppingEngine
import cz.satorigeeks.telegramrpg.model.ItemType
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import kotlin.math.max

/**
 * Handles display and input for the Shop Menu state.
 */
object ShopMenuController {
    val shoppingEngine = ShoppingEngine()
    private const val SELL_MENU = "SELL_MENU"
    private const val BACK_SHOP = "BACK_SHOP"

    /**
     * Shows the shop menu to the user and updates their state.
     */
    suspend fun show(user: User, bot: TelegramBot) {
        val hero = SessionManager.getHero(user)
        val shopItems = shoppingEngine.shopItems

        message { "Welcome to the shop, you have ${hero.money} Gold. What would you like to buy?" }
            .inlineKeyboardMarkup {
                shopItems.forEachIndexed { index, item ->
                    when (item.type) {
                        ItemType.POTION ->
                            "${item.name}, ${item.cost} Gold (Heals ${item.healingPower} HP)" callback index.toString()
                        ItemType.WEAPON ->
                            "${item.name}, ${item.cost} Gold (Attack +${item.attackPower})" callback index.toString()
                    }
                    newLine()
                }
                "\uD83D\uDCB0 Sell from inventory" callback SELL_MENU
                newLine()
                "Leave shop" callback shopItems.size.toString()
            }
            .send(user, bot)
        SessionManager.setState(user, GameState.SHOP_MENU)
    }

    private fun sellPrice(cost: Int): Int =
        max(1, cost / 2)

    private suspend fun showSellMenu(user: User, bot: TelegramBot) {
        val hero = SessionManager.getHero(user)
        val inventory = hero.getInventory()

        message { "Select an item to sell." }.inlineKeyboardMarkup {
            inventory.forEachIndexed { index, item ->
                if (item != null) {
                    val price = sellPrice(item.cost)
                    when (item.type) {
                        ItemType.POTION ->
                            "${item.name} (Heals ${item.healingPower} HP) - $price Gold" callback "SELL_$index"
                        ItemType.WEAPON -> {
                            val equippedTag = if (item.isEquipped) ", Equipped" else ""
                            "${item.name} (Attack +${item.attackPower}$equippedTag) - $price Gold" callback "SELL_$index"
                        }
                    }
                }
                newLine()
            }
            "Back to shop" callback BACK_SHOP
        }.send(user, bot)
        SessionManager.setState(user, GameState.SHOP_MENU)
    }

    /**
     * Handles user selection from the shop menu.
     */
    suspend fun handle(update: ProcessedUpdate, user: User, bot: TelegramBot) {
        val hero = SessionManager.getHero(user)
        val shopItems = shoppingEngine.shopItems

        val text = requireNonBlankText(update, user, bot) { show(user, bot) } ?: return
        if (text == SELL_MENU) {
            showSellMenu(user, bot)
            return
        }
        if (text == BACK_SHOP) {
            show(user, bot)
            return
        }
        if (text.startsWith("SELL_")) {
            val index = text.removePrefix("SELL_").toIntOrNull()
            val item = if (index != null) hero.takeFromInventory(index) else null
            if (item == null) {
                message { "Invalid choice." }.send(user, bot)
                showSellMenu(user, bot)
                return
            }
            val price = sellPrice(item.cost)
            hero.money += price
            message { "Sold ${item.name} for $price Gold. You now have ${hero.money.toInt()} Gold." }.send(user, bot)
            showSellMenu(user, bot)
            return
        }
        val index = text.toIntOrNull()
        if (index == null || index !in 0..shopItems.size) {
            message { "Invalid choice." }.send(user, bot)
            show(user, bot)
            return
        }

        // Leave shop
        if (index == shopItems.size) {
            message { "Thanks for visiting!" }.send(user, bot)
            MainMenuController.show(user, bot)
            return
        }

        // Purchase selected item
        val selectedItem = shopItems[index]
        val result = shoppingEngine.buy(hero, selectedItem)
        message { result }.send(user, bot)
        show(user, bot)
    }
}
