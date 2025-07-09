package cz.satorigeeks.telegramrpg

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate


enum class GameState { MAIN_MENU, ROAM_MENU, SHOP_MENU }

suspend fun main() {
    val bot = TelegramBot("#token_here#")

    bot.handleUpdates()
}

@CommandHandler(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Welcome to Adventure Quest!" }.send(user, bot)
    showMainMenu(user, bot)
}

private suspend fun showMainMenu(user: User, bot: TelegramBot) {
    message { "What would you like to do?" }.inlineKeyboardMarkup {
        "Roam the world" callback "1"
        newLine()
        "Get character info" callback "2"
        newLine()
        "Rest at inn" callback "3"
        newLine()
        "Shop at store" callback "4"
        newLine()
        "Exit the game" callback "5"
    }.send(user, bot)

    bot.inputListener[user] = GameState.MAIN_MENU.name
}

@InputHandler(["MAIN_MENU"])
suspend fun onMainMenuChoice(update: ProcessedUpdate, user: User, bot: TelegramBot) {
    when (update.text.trim()) {
        "1" -> {
            showRoamMenu(user, bot)
        }

        "2" -> {
            message { "Your character info is shown below:" }.send(user, bot)
            showMainMenu(user, bot)
        }

        "3" -> {
            message { "You are now resting at the inn." }.send(user, bot)
            showMainMenu(user, bot)
        }

        "4" -> {
            showShopMenu(user, bot)
        }

        "5" -> {
            message { "Exiting the game..." }.send(user, bot)
            showMainMenu(user, bot)
        }

        else -> {
            message { "Sorry, wrong choice." }.send(user, bot)
            showMainMenu(user, bot)
        }
    }
}

private suspend fun showRoamMenu(user: User, bot: TelegramBot) {
    message { "What would you like to do in battle?" }.inlineKeyboardMarkup {
        "Attack!" callback "1"
        newLine()
        "Use Inventory Item" callback "2"
        newLine()
        "Attempt to Run Away" callback "3"
        newLine()
        "Autopilot" callback "4"
    }.send(user, bot)

    bot.inputListener[user] = GameState.ROAM_MENU.name
}

@InputHandler(["ROAM_MENU"])
suspend fun onBattleChoice(update: ProcessedUpdate, user: User, bot: TelegramBot) {
    when (update.text.trim()) {
        "1" -> {
            message { "Attack!" }.send(user, bot)
            showRoamMenu(user, bot)
        }

        "2" -> {
            message { "Use Inventory Item" }.send(user, bot)
            showRoamMenu(user, bot)
        }

        "3" -> {
            message { "Attempt to Run Away" }.send(user, bot)
            showRoamMenu(user, bot)
        }

        "4" -> {
            message { "Autopilot" }.send(user, bot)
            showRoamMenu(user, bot)
        }

        else -> {
            message { "Sorry, wrong choice." }.send(user, bot)
            showRoamMenu(user, bot)
        }
    }
}

private suspend fun showShopMenu(user: User, bot: TelegramBot) {
    message { "Welcome to the shop, what would you like to buy?" }
        .inlineKeyboardMarkup {
            "Potion, 10 GOLD (Heals 20 HP)" callback "1"
            newLine()
            "Super Potion, 30 GOLD (Heals 50 HP)" callback "2"
            newLine()
            "Leave shop" callback "3"
        }
        .send(user, bot)

    bot.inputListener[user] = GameState.SHOP_MENU.name
}

@InputHandler(["SHOP_MENU"])
suspend fun onShopChoice(update: ProcessedUpdate, user: User, bot: TelegramBot) {
    when (update.text.trim()) {
        "1" -> {
            message { "Potion bought!" }.send(user, bot)
            showShopMenu(user, bot)
        }

        "2" -> {
            message { "Super Potion bought!" }.send(user, bot)
            showShopMenu(user, bot)
        }

        "3" -> {
            message { "Thanks for visiting!" }.send(user, bot)
            showMainMenu(user, bot)
        }

        else -> {
            message { "Sorry, wrong choice." }.send(user, bot)
            showShopMenu(user, bot)
        }
    }
}

@UnprocessedHandler
suspend fun unprocessed(update: ProcessedUpdate, user: User, bot: TelegramBot) {
    message { update.text }.send(user, bot)
}