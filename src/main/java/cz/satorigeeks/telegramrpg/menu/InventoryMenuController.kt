package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.engine.CombatEngine.enemyAttack
import cz.satorigeeks.telegramrpg.engine.CombatEngine.resolve
import cz.satorigeeks.telegramrpg.menu.MainMenuController.MainMenuAction
import cz.satorigeeks.telegramrpg.menu.ShopMenuController.shoppingEngine
import cz.satorigeeks.telegramrpg.model.Enemy
import cz.satorigeeks.telegramrpg.model.Hero
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import kotlin.math.roundToInt

object InventoryMenuController {

    /**
     * Shows the inventory content to the user.
     */
    suspend fun show(user: User, bot: TelegramBot) {
        val hero = SessionManager.getHero(user)
        val inventory = hero.getInventory()

        message { "Pick the item you'd like to use." }.inlineKeyboardMarkup {
            inventory.forEachIndexed { index, item ->
                if (item != null)
                    "${item.name} (Heals ${item.healingPower} HP)" callback index.toString()
                newLine()
            }
            "Back to battle" callback inventory.size.toString()
        }.send(user, bot)
        SessionManager.setState(user, GameState.INVENTORY_MENU)
    }

    /**
     * Handles user selection from the inventory menu.
     */
    suspend fun handle(update: ProcessedUpdate, user: User, bot: TelegramBot) {
        val hero = SessionManager.getHero(user)
        val inventory = hero.getInventory()

        val index = update.text.trim().toIntOrNull()

        // Leave the inventory
        if (index == inventory.size) {
            RoamMenuController.show(user, bot)
            return
        }

        // Pass the selected item into roam handler, so it is used.
        if (index != null && index in 0..inventory.size - 1) {
            val selectedItem = inventory[index]

            SessionManager.setItemToBeUsed(user, selectedItem)
            hero.removeFromInventory(index)
        } else {
            message { "Invalid choice." }.send(user, bot)
            show(user, bot)
        }

        RoamMenuController.handle(update, user, bot)
    }
}