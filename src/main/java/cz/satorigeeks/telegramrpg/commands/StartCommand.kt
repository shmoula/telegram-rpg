package cz.satorigeeks.telegramrpg.commands

import cz.satorigeeks.telegramrpg.menu.MainMenuController
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User

/**
 * Handles the /start command to kick off the Adventure Quest game.
 */
@CommandHandler(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Welcome to Adventure Quest!" }.send(user, bot)
    MainMenuController.show(user, bot)
}
