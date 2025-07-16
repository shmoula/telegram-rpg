package cz.satorigeeks.telegramrpg

import cz.satorigeeks.telegramrpg.menu.InventoryMenuController
import cz.satorigeeks.telegramrpg.menu.MainMenuController
import cz.satorigeeks.telegramrpg.menu.RoamMenuController
import cz.satorigeeks.telegramrpg.menu.ShopMenuController
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import io.github.cdimascio.dotenv.dotenv

/**
 * Entry point and central dispatcher for the bot.
 */
suspend fun main() {
    val env = dotenv {
        directory = "./"
        ignoreIfMissing = true
    }

    val token = env["TELEGRAM_BOT_TOKEN"]
        ?: System.getenv("TELEGRAM_BOT_TOKEN")
        ?: error("Missing TELEGRAM_BOT_TOKEN in environment")

    val bot = TelegramBot(token)

    bot.handleUpdates()
}

/**
 * Catches all other text inputs and routes them based on the user's state.
 */
@UnprocessedHandler
suspend fun dispatch(update: ProcessedUpdate, user: User, bot: TelegramBot) {
    when (SessionManager.getState(user)) {
        GameState.MAIN_MENU -> MainMenuController.handle(update, user, bot)
        GameState.ROAM_MENU -> RoamMenuController.handle(update, user, bot)
        GameState.SHOP_MENU -> ShopMenuController.handle(update, user, bot)
        GameState.INVENTORY_MENU -> InventoryMenuController.handle(update, user, bot)
    }
}