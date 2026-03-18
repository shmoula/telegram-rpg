package cz.satorigeeks.telegramrpg.commands

import cz.satorigeeks.telegramrpg.Logger
import cz.satorigeeks.telegramrpg.menu.MainMenuController
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User

/**
 * Handles the /restart command and resets the game.
 */
@CommandHandler(["/restart"])
suspend fun restart(user: User, bot: TelegramBot) {
    Logger.log(user, "RESTART")
    SessionManager.reset(user)
    message { "Game restarted! Your progress has been reset." }.send(user, bot)
    MainMenuController.show(user, bot)
}
