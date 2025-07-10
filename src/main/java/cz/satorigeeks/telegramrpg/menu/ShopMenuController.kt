package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.engine.ShoppingEngine
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Handles display and input for the Shop Menu state.
 */
object ShopMenuController {
    val shoppingEngine = ShoppingEngine()

    /**
     * Shows the shop menu to the user and updates their state.
     */
    suspend fun show(user: User, bot: TelegramBot) {
        val hero = SessionManager.getHero(user)
        val shopItems = shoppingEngine.shopItems

        message { "Welcome to the shop, you have ${hero.money} Gold. What would you like to buy?" }
            .inlineKeyboardMarkup {
                shopItems.forEachIndexed { index, item ->
                    "${item.name}, ${item.cost} Gold (Heals ${item.healingPower} HP)" callback index.toString()
                    newLine()
                }
                "Leave shop" callback shopItems.size.toString()
            }
            .send(user, bot)
        SessionManager.setState(user, GameState.SHOP_MENU)
    }

    /**
     * Handles user selection from the shop menu.
     */
    suspend fun handle(update: ProcessedUpdate, user: User, bot: TelegramBot) {
        val hero = SessionManager.getHero(user)
        val shopItems = shoppingEngine.shopItems

        val index = update.text.trim().toIntOrNull()
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
