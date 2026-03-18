package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.Logger
import cz.satorigeeks.telegramrpg.engine.CombatEngine
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ParseMode
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
                "⚔\uFE0F Attack the ${enemy.name}!" callback RoamMenuAction.ATTACK.name
                newLine()
                "\uD83C\uDF92 Use Inventory Item" callback RoamMenuAction.INVENTORY.name
                newLine()
                "\uD83C\uDFC3\u200D♂\uFE0F Attempt to Run Away" callback RoamMenuAction.RUN.name
                newLine()
                "\uD83E\uDD16 Autopilot" callback RoamMenuAction.AUTOPILOT.name
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
        var combatState: CombatEngine.CombatState? = null
        val text = requireNonBlankText(update, user, bot) { show(user, bot) } ?: return

        if (enemy == null) {
            MainMenuController.show(user, bot)
            return
        }

        val action = enumValues<RoamMenuAction>().find { it.name == text }
        when (action) {
            RoamMenuAction.ATTACK -> {
                Logger.log(user, "ATTACK", "${hero.name} vs ${enemy.name}")
                combatState = CombatEngine.fight(hero, enemy, heroFirst)

                // Print the combat round details according to the order.
                val attackOrder = if (heroFirst)
                    listOf(combatState.heroAttackResult, combatState.enemyAttackResult)
                else
                    listOf(combatState.enemyAttackResult, combatState.heroAttackResult)

                attackOrder.forEach {
                    message { CombatEngine.resolve(it) }.send(user, bot)
                }
            }

            RoamMenuAction.INVENTORY -> {
                if (hero.isInventoryEmpty())
                    message { "❌ Your inventory is empty." }.send(user, bot)
                else {
                    InventoryMenuController.show(user, bot)
                    return
                }
            }

            RoamMenuAction.RUN -> {
                if (heroFirst) {
                    if (hero.runAway()) {
                        Logger.log(user, "RUN", "escaped from ${enemy.name}")
                        message { "\uD83C\uDFC3\u200D♂\uFE0F You successfully ran away." }.send(user, bot)
                        combatState = CombatEngine.CombatState(CombatEngine.CombatState.CombatResult.FLED)
                    } else {
                        Logger.log(user, "RUN", "failed to escape from ${enemy.name}")
                        message { "❌ Failed to run away!" }.send(user, bot)
                        combatState = CombatEngine.fight(hero, enemy, heroFirst = true, onlyEnemy = true)
                        message { CombatEngine.resolve(combatState.enemyAttackResult) }.send(user, bot)
                    }
                } else {
                    combatState = CombatEngine.fight(hero, enemy, heroFirst = false, onlyEnemy = true)
                    message { CombatEngine.resolve(combatState!!.enemyAttackResult) }.send(user, bot)

                    if (hero.runAway()) {
                        Logger.log(user, "RUN", "escaped from ${enemy.name} (after enemy turn)")
                        message { "\uD83C\uDFC3\u200D♂\uFE0F You successfully ran away." }.send(user, bot)
                        combatState = CombatEngine.CombatState(CombatEngine.CombatState.CombatResult.FLED)
                    } else {
                        Logger.log(user, "RUN", "failed to escape from ${enemy.name} (after enemy turn)")
                        message { "❌ Failed to run away!" }.send(user, bot)
                    }
                }
            }

            RoamMenuAction.AUTOPILOT -> {
                Logger.log(user, "AUTOPILOT", "battling ${enemy.name}")
                message { "\uD83E\uDD16 Autopilot engaged! Battling on your behalf." }.send(user, bot)
                val maxRounds = 10
                var rounds = 0
                while (hero.isAlive && enemy.isAlive && rounds < maxRounds) {
                    combatState = CombatEngine.fight(hero, enemy, heroFirst)

                    // Print the combat round details according to the order.
                    val attackOrder = if (heroFirst)
                        listOf(combatState.heroAttackResult, combatState.enemyAttackResult)
                    else
                        listOf(combatState.enemyAttackResult, combatState.heroAttackResult)

                    val roundSummary = attackOrder
                        .map { CombatEngine.resolve(it) }
                        .filter { it.isNotBlank() }
                        .joinToString("\n")
                    if (roundSummary.isNotBlank()) {
                        message { roundSummary }.send(user, bot)
                    }
                    rounds++
                }
                if (rounds >= maxRounds && hero.isAlive && enemy.isAlive) {
                    message { "⚠️ Autopilot paused after $maxRounds rounds to avoid chat spam." }.send(user, bot)
                }
            }

            // We probably came from the InventoryMenuController, so we should consume the passed item
            null -> {
                val itemToBeUsed = SessionManager.getItemToBeUsed(user)
                if (itemToBeUsed != null) {
                    if (heroFirst) {
                        val useMessage = hero.useItem(itemToBeUsed)
                        SessionManager.setItemToBeUsed(user, null)

                        message { useMessage }.send(user, bot)

                        combatState = CombatEngine.fight(hero, enemy, heroFirst = true, onlyEnemy = true)
                        message { CombatEngine.resolve(combatState.enemyAttackResult) }.send(user, bot)
                    } else {
                        combatState = CombatEngine.fight(hero, enemy, heroFirst = false, onlyEnemy = true)
                        message { CombatEngine.resolve(combatState.enemyAttackResult) }.send(user, bot)

                        if (hero.isAlive) {
                            val useMessage = hero.useItem(itemToBeUsed)
                            SessionManager.setItemToBeUsed(user, null)

                            message { useMessage }.send(user, bot)
                        }
                    }
                }
            }
        }

        SessionManager.saveHero(user)

        if (combatState == null)
            show(user, bot)
        else
            when (combatState.combatResult) {
                CombatEngine.CombatState.CombatResult.CONTINUE -> {
                    message {
                        "\uD83D\uDCCA Status: ${hero.name} HP = ${hero.health.toInt()} | ${enemy.name} HP = ${enemy.health.toInt()}"
                    }.send(user, bot)
                    show(user, bot)
                }

                CombatEngine.CombatState.CombatResult.LOSS -> {
                    Logger.log(user, "DEFEAT", "${hero.name} was defeated by ${enemy.name}")
                    message { "\uD83D\uDC80 ${hero.name} has fallen. Game over. Use /restart to try again." }.send(
                        user,
                        bot
                    )
                    message { hero.getInfo() }
                        .options { parseMode = ParseMode.Markdown }
                        .send(user, bot)
                }

                CombatEngine.CombatState.CombatResult.VICTORY -> {
                    Logger.log(user, "VICTORY", "${hero.name} defeated ${enemy.name}, earned ${combatState.gold} gold and ${combatState.exp} XP")
                    message {
                        "\uD83C\uDFC6 You have vanquished the beast and received ${combatState.gold} Gold and ${combatState.exp} experience!"
                    }.send(user, bot)
                    combatState.lootItem?.let { item ->
                        val lootMessage = if (combatState.lootAdded) {
                            "\uD83C\uDF81 The enemy dropped ${item.name}, added to your inventory."
                        } else {
                            "\uD83D\uDCE6 The enemy dropped ${item.name}, but your inventory is full."
                        }
                        message { lootMessage }.send(user, bot)
                    }
                    hero.consumeLevelUpMessage()?.let { levelUpMessage ->
                        message { levelUpMessage }.send(user, bot)
                    }
                    MainMenuController.show(user, bot)
                }

                CombatEngine.CombatState.CombatResult.FLED -> {
                    MainMenuController.show(user, bot)
                }
            }
    }
}
