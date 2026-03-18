package cz.satorigeeks.telegramrpg.menu

import cz.satorigeeks.telegramrpg.Logger
import cz.satorigeeks.telegramrpg.model.ItemType
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

object InventoryMenuController {

    /**
     * Shows the inventory content to the user.
     */
    suspend fun show(user: User, bot: TelegramBot, returnToMainMenu: Boolean = false) {
        val hero = SessionManager.getHero(user)
        val inventory = hero.getInventory()
        SessionManager.setInventoryReturnToMainMenu(user, returnToMainMenu)

        message { "Pick the item you'd like to use or equip." }.inlineKeyboardMarkup {
            inventory.forEachIndexed { index, item ->
                if (item != null) {
                    when (item.type) {
                        ItemType.POTION ->
                            "${item.name} (Heals ${item.healingPower} HP)" callback index.toString()

                        ItemType.WEAPON -> {
                            val equippedTag = if (item.isEquipped) ", Equipped" else ""
                            "${item.name} (Attack +${item.attackPower}$equippedTag)" callback index.toString()
                        }
                    }
                }
                newLine()
            }
            val backLabel = if (returnToMainMenu) "Back to menu" else "Back to battle"
            backLabel callback inventory.size.toString()
        }.send(user, bot)
        SessionManager.setState(user, GameState.INVENTORY_MENU)
    }

    /**
     * Handles user selection from the inventory menu.
     */
    suspend fun handle(update: ProcessedUpdate, user: User, bot: TelegramBot) {
        val hero = SessionManager.getHero(user)
        val inventory = hero.getInventory()

        val returnToMainMenu = SessionManager.getInventoryReturnToMainMenu(user)
        val text = requireNonBlankText(update, user, bot) { show(user, bot, returnToMainMenu) } ?: return
        val index = text.toIntOrNull()

        // Leave the inventory
        if (index == inventory.size) {
            if (returnToMainMenu) {
                MainMenuController.show(user, bot)
            } else {
                RoamMenuController.show(user, bot)
            }
            return
        }

        // Pass the selected item into roam handler, so it is used.
        if (index != null && index in 0..inventory.size - 1) {
            val selectedItem = inventory[index]

            if (selectedItem == null) {
                message { "Invalid choice." }.send(user, bot)
                show(user, bot, returnToMainMenu)
                return
            }

            if (returnToMainMenu) {
                Logger.log(user, "USE_ITEM", "${selectedItem.name} (from menu)")
                val useMessage = hero.useItem(selectedItem)
                if (selectedItem.type == ItemType.POTION) {
                    hero.removeFromInventory(index)
                }
                message { useMessage }.send(user, bot)
                SessionManager.saveHero(user)
                MainMenuController.show(user, bot)
                return
            } else {
                Logger.log(user, "USE_ITEM", "${selectedItem.name} (in battle)")
                SessionManager.setItemToBeUsed(user, selectedItem)
                if (selectedItem.type == ItemType.POTION) {
                    hero.removeFromInventory(index)
                }
                SessionManager.saveHero(user)
            }
        } else {
            message { "Invalid choice." }.send(user, bot)
            show(user, bot, returnToMainMenu)
            return
        }

        RoamMenuController.handle(update, user, bot)
    }
}
