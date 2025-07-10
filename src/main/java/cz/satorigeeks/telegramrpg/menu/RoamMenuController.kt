package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.model.Bestiary
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Handles display and input for the Roam/Battle Menu state.
 */
object RoamMenuController {
    enum class RoamMenuAction {
        ATTACK,
        INVENTORY,
        RUN,
        AUTOPILOT
    }

    /**
     * Shows the roam (battle) menu to the user and updates their state.
     */
    suspend fun show(user: User, bot: TelegramBot, firstEntry: Boolean = false) {
        val hero = SessionManager.getHero(user)
        val enemy = Bestiary.gimmeBeast(hero)

        if (firstEntry)
            message {
                "You encounter a wild '${enemy.name}' with HP = ${enemy.health.toInt()} and MP = ${enemy.magicPower}!"
            }.send(user, bot)

        message { "What would you like to do in battle?" }
            .inlineKeyboardMarkup {
                "Attack!" callback RoamMenuAction.ATTACK.name
                newLine()
                "Use Inventory Item" callback RoamMenuAction.INVENTORY.name
                newLine()
                "Attempt to Run Away" callback RoamMenuAction.RUN.name
                newLine()
                "Autopilot" callback RoamMenuAction.AUTOPILOT.name
            }
            .send(user, bot)
        SessionManager.setState(user, GameState.ROAM_MENU)
    }

    /**
     * Handles user selection from the roam menu.
     */
    suspend fun handle(update: ProcessedUpdate, user: User, bot: TelegramBot) {
        when (RoamMenuAction.valueOf(update.text)) {
            RoamMenuAction.ATTACK -> {
                message { "You swing your sword and strike the enemy!" }.send(user, bot)
                show(user, bot)
            }

            RoamMenuAction.INVENTORY -> {
                message { "Opening inventory... which item to use?" }.send(user, bot)
                show(user, bot)
            }

            RoamMenuAction.RUN -> {
                message { "You attempt to run away..." }.send(user, bot)
                show(user, bot)
            }

            RoamMenuAction.AUTOPILOT -> {
                message { "Autopilot engaged! Battling on your behalf." }.send(user, bot)
                show(user, bot)
            }
        }
    }
}
