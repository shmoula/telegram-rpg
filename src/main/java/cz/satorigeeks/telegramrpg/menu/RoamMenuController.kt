package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.engine.CombatEngine
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
    suspend fun show(user: User, bot: TelegramBot) {
        val enemy = SessionManager.getEnemy(user)

        if (enemy == null) {
            MainMenuController.show(user, bot)
            return
        }

        message { "What would you like to do in battle?" }
            .inlineKeyboardMarkup {
                "Attack the ${enemy.name}!" callback RoamMenuAction.ATTACK.name
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
        val hero = SessionManager.getHero(user)
        val enemy = SessionManager.getEnemy(user)
        val heroFirst = SessionManager.getHeroFirst(user)

        if (enemy == null) {
            MainMenuController.show(user, bot)
            return
        }

        when (RoamMenuAction.valueOf(update.text)) {
            RoamMenuAction.ATTACK -> {
                val combatState = CombatEngine.fight(hero, enemy, heroFirst)

                // Print the combat round details according to the order.
                val attackOrder = if (heroFirst)
                    listOf(combatState.heroAttackResult, combatState.enemyAttackResult)
                else
                    listOf(combatState.enemyAttackResult, combatState.heroAttackResult)

                attackOrder.forEach {
                    message { CombatEngine.resolve(it) }.send(user, bot)
                }

                when (combatState.combatResult) {
                    CombatEngine.CombatState.CombatResult.CONTINUE -> {
                        message {
                            "Status: ${hero.name} HP = ${hero.health.toInt()} | ${enemy.name} HP = ${enemy.health.toInt()}"
                        }.send(user, bot)
                        show(user, bot)
                    }

                    CombatEngine.CombatState.CombatResult.LOSS -> {
                        message { "${hero.name} has fallen. Game over." }.send(user, bot)
                        MainMenuController.show(user, bot)
                    }

                    CombatEngine.CombatState.CombatResult.VICTORY -> {
                        message {
                            "You have vanquished the beast and received ${combatState.gold} Gold and ${combatState.exp} experience!"
                        }.send(user, bot)
                        MainMenuController.show(user, bot)
                    }
                }
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
