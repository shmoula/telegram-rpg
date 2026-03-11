package cz.satorigeeks.telegramrpg.menu

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Helper for returning a non-blank text from processed update
 * @param update processed update
 * @param user user who sent the update
 * @param bot bot instance
 * @param onInvalid callback to be called when text is blank
 * @return non-blank text or null if invalid
 */
suspend fun requireNonBlankText(
    update: ProcessedUpdate,
    user: User,
    bot: TelegramBot,
    onInvalid: suspend () -> Unit
): String? {
    val text = update.text?.trim()
    if (text.isNullOrBlank()) {
        message { "Invalid choice." }.send(user, bot)
        onInvalid()
        return null
    }
    return text
}
