package cz.satorigeeks.telegramrpg

import cz.satorigeeks.telegramrpg.db.Users
import cz.satorigeeks.telegramrpg.menu.InventoryMenuController
import cz.satorigeeks.telegramrpg.menu.MainMenuController
import cz.satorigeeks.telegramrpg.menu.RoamMenuController
import cz.satorigeeks.telegramrpg.menu.ShopMenuController
import cz.satorigeeks.telegramrpg.state.GameState
import cz.satorigeeks.telegramrpg.state.SessionManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.api.botactions.setMyCommands
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction


/**
 * Entry point and central dispatcher for the bot.
 */
suspend fun main() {
    val env = dotenv {
        directory = "./"
        ignoreIfMissing = true
    }

    val token = env["TELEGRAM_BOT_TOKEN"]
        ?: System.getenv("TELEGRAM_BOT_TOKEN")
        ?: error("Missing TELEGRAM_BOT_TOKEN in environment")

    val bot = TelegramBot(token)

    setMyCommands {
        BotCommandScope.Default()
        BotCommand("start", "Starts your adventure.")
        BotCommand("info", "Shows info about the bot & developer.")
        BotCommand("check_stats", "Shows player stats.")
        BotCommand("restart", "Resets your game progress.")
    }.send(bot)

    initDatabase()

    bot.handleUpdates()
}

/**
 * Catches all other text inputs and routes them based on the user's state.
 */
@UnprocessedHandler
suspend fun dispatch(update: ProcessedUpdate, user: User, bot: TelegramBot) {
    when (SessionManager.getState(user)) {
        GameState.MAIN_MENU -> MainMenuController.handle(update, user, bot)
        GameState.ROAM_MENU -> RoamMenuController.handle(update, user, bot)
        GameState.SHOP_MENU -> ShopMenuController.handle(update, user, bot)
        GameState.INVENTORY_MENU -> InventoryMenuController.handle(update, user, bot)
    }
}

/**
 * Create database connection and create tables if needed.
 */
fun initDatabase() {
    val dbPath = System.getenv("DB_PATH") ?: "./game.db"

    Database.connect("jdbc:sqlite:$dbPath", driver = "org.sqlite.JDBC")

    transaction {
        // Automatically creates the table if it doesn't exist
        SchemaUtils.create(Users)
    }
}