package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.StateManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Handles display and input for the Roam/Battle Menu state.
 */
object RoamMenuController {
    /**
     * Shows the roam (battle) menu to the user and updates their state.
     */
    suspend fun show(user: User, bot: TelegramBot) {
        message { "What would you like to do in battle?" }
            .inlineKeyboardMarkup {
                "Attack!" callback "1"
                newLine()
                "Use Inventory Item" callback "2"
                newLine()
                "Attempt to Run Away" callback "3"
                newLine()
                "Autopilot" callback "4"
            }
            .send(user, bot)
        StateManager.set(user, GameState.ROAM_MENU)
    }

    /**
     * Handles user selection from the roam menu.
     */
    suspend fun handle(update: ProcessedUpdate, user: User, bot: TelegramBot) {
        when (update.text.trim()) {
            "1" -> {
                message { "You swing your sword and strike the enemy!" }.send(user, bot)
                show(user, bot)
            }

            "2" -> {
                message { "Opening inventory... which item to use?" }.send(user, bot)
                // you might route to an InventoryController here
                show(user, bot)
            }

            "3" -> {
                message { "You attempt to run away..." }.send(user, bot)
                // optionally change state or resolve outcome
                show(user, bot)
            }

            "4" -> {
                message { "Autopilot engaged! Battling on your behalf." }.send(user, bot)
                show(user, bot)
            }

            else -> {
                message { "Sorry, wrong choice." }.send(user, bot)
                show(user, bot)
            }
        }
    }
}
