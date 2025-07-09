package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.StateManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Handles display and input for the Shop Menu state.
 */
object ShopMenuController {
    /**
     * Shows the shop menu to the user and updates their state.
     */
    suspend fun show(user: User, bot: TelegramBot) {
        message { "Welcome to the shop, what would you like to buy?" }
            .inlineKeyboardMarkup {
                "Potion, 10 GOLD (Heals 20 HP)" callback "1"
                newLine()
                "Super Potion, 30 GOLD (Heals 50 HP)" callback "2"
                newLine()
                "Leave shop" callback "3"
            }
            .send(user, bot)
        StateManager.set(user, GameState.SHOP_MENU)
    }

    /**
     * Handles user selection from the shop menu.
     */
    suspend fun handle(update: ProcessedUpdate, user: User, bot: TelegramBot) {
        when (update.text.trim()) {
            "1" -> {
                message { "Potion bought!" }.send(user, bot)
                show(user, bot)
            }

            "2" -> {
                message { "Super Potion bought!" }.send(user, bot)
                show(user, bot)
            }

            "3" -> {
                message { "Thanks for visiting!" }.send(user, bot)
                MainMenuController.show(user, bot)
            }

            else -> {
                message { "Sorry, wrong choice." }.send(user, bot)
                show(user, bot)
            }
        }
    }
}
