package cz.satorigeeks.telegramrpg.model

/**
 * Holds all possible enemies and returns a suitable one based on Hero level.
 */
object Bestiary {
    private val bestiary = listOf(
        Enemy("Cave Rat", 20.0, 4.0, 0),
        Enemy("Spiderling", 25.0, 5.0, 5),
        Enemy("Slime", 30.0, 5.0, 0),
        Enemy("Venomous Snake", 35.0, 6.0, 10),
        Enemy("Burning Ember", 40.0, 3.0, 15),
        Enemy("Shambling Corpse", 45.0, 7.0, 0),
        Enemy("Ubda", 50.0, 10.0, 20),
        Enemy("Giant Crab", 55.0, 8.0, 10),
        Enemy("Giant Rat", 60.0, 12.0, 0),
        Enemy("Giant Spider", 65.0, 15.0, 15),
        Enemy("Skeleton Warrior", 70.0, 18.0, 0),
        Enemy("Giant Bat", 75.0, 20.0, 25),
        Enemy("Goblin", 80.0, 15.0, 5),
        Enemy("Goblin Archer", 85.0, 20.0, 10),
        Enemy("Dark Elf", 90.0, 10.0, 30),
        Enemy("Goblin Shaman", 95.0, 15.0, 15),
        Enemy("Goblin King", 100.0, 25.0, 0),
        Enemy("Goblin Wizard", 105.0, 15.0, 20),
        Enemy("Shadow Fiend", 110.0, 25.0, 35),
        Enemy("Goblin Captain", 115.0, 30.0, 0),
        Enemy("Orc", 120.0, 20.0, 10),
        Enemy("Orc Warrior", 125.0, 25.0, 15),
        Enemy("Vampire", 130.0, 20.0, 25),
        Enemy("Orc Shaman", 135.0, 30.0, 20),
        Enemy("Orc Lord", 140.0, 35.0, 0),
        Enemy("Orc Wizard", 145.0, 30.0, 30),
        Enemy("Fire Elemental", 150.0, 30.0, 40),
        Enemy("Orc Captain", 155.0, 40.0, 0),
        Enemy("Giant", 160.0, 40.0, 25),
        Enemy("Giant Wolf", 165.0, 50.0, 0),
        Enemy("Giant Ogre", 170.0, 45.0, 30),
        Enemy("Giant Bear", 175.0, 55.0, 0),
        Enemy("Wyvern", 180.0, 35.0, 15),
        Enemy("Giant Eagle", 185.0, 60.0, 0),
        Enemy("Giant Snake", 190.0, 40.0, 20),
        Enemy("Giant Lizard", 195.0, 50.0, 0),
        Enemy("Troll", 200.0, 25.0, 0),
        Enemy("Ice Golem", 250.0, 15.0, 20)
    )

    fun gimmeBeast(hero: Hero): Enemy {
        val limit = when {
            hero.level <= 2 -> 50.0
            hero.level <= 5 -> 100.0
            hero.level <= 10 -> 150.0
            else -> 250.0
        }
        return bestiary.filter { it.health <= limit }
            .let { it.shuffled()[0] }
    }
}