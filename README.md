# Telegram RPG

A simple text-based RPG game running as a Telegram bot. The player can explore the world, encounter creatures, shop for potions, and rest to recover health. This project serves as a lightweight prototype for experimenting with Telegram bot interactions and game logic in Kotlin.

## Features

* 🌍 **Roam**: Explore the world and face random enemy encounters.
* ⚔️ **Fight**: Battle creatures, earn XP, and collect loot.
* 🛒 **Shop**: Purchase healing potions to aid you in combat.
* 🏨 **Rest**: Recover your health at the inn.
* 🧪 **Potions**: Use items from your inventory to survive tougher encounters.

## Motivation

This game was built as a sandbox to understand how Telegram bots work and to prototype potential mechanics for a larger game project. The focus is on basic interactivity and basic gameplay through Telegram’s interface.

## Getting Started

### Prerequisites

* Kotlin (JVM-based)
* Telegram bot token from [BotFather](https://t.me/BotFather)

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/shmoula/telegram-rpg
   cd telegram-rpg
   ```

2. Create a `.env` file in the project root:

   ```
   TELEGRAM_BOT_TOKEN=your_telegram_bot_token_here
   ```

3. Run the bot:

   ```bash
   ./gradlew run
   ```
