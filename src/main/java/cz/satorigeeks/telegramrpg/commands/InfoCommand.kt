package cz.satorigeeks.telegramrpg.commands

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User

/**
 * Handles the /info command.
 */
@CommandHandler(["/info"])
suspend fun info(user: User, bot: TelegramBot) {
    message { "A simple RPG game. Source code available at https://github.com/shmoula/telegram-rpg" }.send(user, bot)
}
