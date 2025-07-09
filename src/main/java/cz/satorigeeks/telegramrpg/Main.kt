package cz.satorigeeks.telegramrpg

import cz.satorigeeks.telegramrpg.menu.MainMenuController
import cz.satorigeeks.telegramrpg.menu.RoamMenuController
import cz.satorigeeks.telegramrpg.menu.ShopMenuController
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.StateManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Entry point and central dispatcher for the bot.
 */
suspend fun main() {
    val bot = TelegramBot("#bot_token_here#")

    bot.handleUpdates()
}

/**
 * Handles the /start command to kick off the conversation.
 */
@CommandHandler(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Welcome to Adventure Quest!" }.send(user, bot)
    MainMenuController.show(user, bot)
}

/**
 * Catches all other text inputs and routes them based on the user's state.
 */
@UnprocessedHandler
suspend fun dispatch(update: ProcessedUpdate, user: User, bot: TelegramBot) {
    when (StateManager.get(user)) {
        GameState.MAIN_MENU -> MainMenuController.handle(update, user, bot)
        GameState.ROAM_MENU -> RoamMenuController.handle(update, user, bot)
        GameState.SHOP_MENU -> ShopMenuController.handle(update, user, bot)
        else -> message {
            "I didn't understand that. Please type /start to begin."
        }.send(user, bot)
    }
}