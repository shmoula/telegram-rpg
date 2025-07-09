package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.StateManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Handles display and input for the Main Menu state.
 */
object MainMenuController {
    /**
     * Shows the main menu to the user and sets their state.
     */
    suspend fun show(user: User, bot: TelegramBot) {
        message { "What would you like to do?" }.inlineKeyboardMarkup {
            "Roam the world" callback "1"
            newLine()
            "Get character info" callback "2"
            newLine()
            "Rest at inn" callback "3"
            newLine()
            "Shop at store" callback "4"
            newLine()
            "Exit the game" callback "5"
        }.send(user, bot)
        StateManager.set(user, GameState.MAIN_MENU)
    }

    /**
     * Handles user selection from the main menu.
     */
    suspend fun handle(update: ProcessedUpdate, user: User, bot: TelegramBot) {
        when (update.text.trim()) {
            "1" -> RoamMenuController.show(user, bot)
            "2" -> {
                message { "Your character info is shown below:" }.send(user, bot)
                show(user, bot)
            }

            "3" -> {
                message { "You are now resting at the inn." }.send(user, bot)
                show(user, bot)
            }

            "4" -> ShopMenuController.show(user, bot)
            "5" -> {
                message { "Exiting the game..." }.send(user, bot)
                show(user, bot)
            }

            else -> {
                message { "Sorry, wrong choice." }.send(user, bot)
                show(user, bot)
            }
        }
    }
}