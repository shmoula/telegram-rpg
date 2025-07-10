package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.engine.WorldEngine
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Handles display and input for the Main Menu state.
 */
object MainMenuController {
    enum class MainMenuAction(val callback: String) {
        ROAM("ROAM"),
        INFO("INFO"),
        REST("REST"),
        SHOP("SHOP");

        companion object {
            fun fromCallback(cb: String) = entries.firstOrNull { it.callback == cb }
        }
    }

    /**
     * Shows the main menu to the user and sets their state.
     */
    suspend fun show(user: User, bot: TelegramBot) {
        message { "What would you like to do?" }.inlineKeyboardMarkup {
            "Roam the world" callback MainMenuAction.ROAM.callback
            newLine()
            "Get character info" callback MainMenuAction.INFO.callback
            newLine()
            "Rest at inn" callback MainMenuAction.REST.callback
            newLine()
            "Shop at store" callback MainMenuAction.SHOP.callback
        }.send(user, bot)
        SessionManager.setState(user, GameState.MAIN_MENU)
    }

    /**
     * Handles user selection from the main menu.
     */
    suspend fun handle(update: ProcessedUpdate, user: User, bot: TelegramBot) {
        val hero = SessionManager.getHero(user)

        when (MainMenuAction.fromCallback(update.text.trim())) {
            MainMenuAction.ROAM -> RoamMenuController.show(user, bot)
            MainMenuAction.INFO -> {
                message { hero.getInfo() }.send(user, bot)
                show(user, bot)
            }

            MainMenuAction.REST -> {
                message {
                    WorldEngine.rest(hero)
                }.send(user, bot)
                show(user, bot)
            }

            MainMenuAction.SHOP -> ShopMenuController.show(user, bot)

            else -> {
                message { "Sorry, wrong choice." }.send(user, bot)
                show(user, bot)
            }
        }
    }
}