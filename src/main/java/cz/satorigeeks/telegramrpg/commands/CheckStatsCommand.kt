package cz.satorigeeks.telegramrpg.commands

import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User

/**
 * Handles the /check_stats command.
 */
@CommandHandler(["/check_stats"])
suspend fun checkStats(user: User, bot: TelegramBot) {
    val hero = SessionManager.getHero(user)
    message { hero.getInfo() }.send(user, bot)
}
