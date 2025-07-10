package cz.satorigeeks.telegramrpg.engine

import cz.satorigeeks.telegramrpg.model.Hero


/**
 * Non-combat world actions (resting, shopping, etc.).
 */
object WorldEngine {

    /**
     * Heals the hero and returns informative message.
     */
    fun rest(hero: Hero): String {
        hero.health += 100.0
        return "Time has passed. You feel rested and ready for more adventure! (HP = ${hero.health.toInt()})"
    }
}