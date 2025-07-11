package cz.satorigeeks.telegramrpg.engine

import cz.satorigeeks.telegramrpg.model.Enemy
import cz.satorigeeks.telegramrpg.model.Hero
import kotlin.random.Random

/**
 * Handles the combat logic between Hero and Enemy.
 */
object CombatEngine {
    // Represents the result of an attack.
    data class AttackResult(
        val attacker: String,
        val target: String,
        val damage: Double,
        val wasSpecial: Boolean
    )

    // Represents the current state of the battle.
    data class CombatState(
        val combatResult: CombatResult,
        val heroAttackResult: AttackResult? = null,
        val enemyAttackResult: AttackResult? = null,
        val gold: Int = 0,
        val exp: Int = 0
    ) {
        enum class CombatResult {
            LOSS, VICTORY, CONTINUE, FLED
        }
    }

    /**
     * Runs one round of a fight. According to the order attacks are led and results returned.
     * @param hero The hero participating in the fight.
     * @param enemy The enemy participating in the fight.
     * @param heroFirst Boolean indicating whether the hero attacks first.
     * @param failedFlee Boolean indicating whether the hero failed to flee.
     * @return A CombatState object representing the result of the fight round.
     */
    fun fight(hero: Hero, enemy: Enemy, heroFirst: Boolean, failedFlee: Boolean = false): CombatState {
        var heroAttackResult: AttackResult? = null
        var enemyAttackResult: AttackResult? = null

        // On failed flee allow only enemy to attack, since player already did his turn (attempt to flee)
        if (failedFlee) {
            enemyAttackResult = enemyAttack(enemy, hero)
        } else if (heroFirst) {
            heroAttackResult = heroAttack(hero, enemy)
            if (enemy.isAlive)
                enemyAttackResult = enemyAttack(enemy, hero)
        } else {
            enemyAttackResult = enemyAttack(enemy, hero)
            if (hero.isAlive)
                heroAttackResult = heroAttack(hero, enemy)
        }

        return when {
            !hero.isAlive -> CombatState(CombatState.CombatResult.LOSS, heroAttackResult, enemyAttackResult)

            !enemy.isAlive -> {
                val gold = Random.nextInt(10, 51)
                val exp = Random.nextInt(20, 40)

                hero.money += gold
                hero.experience += exp
                CombatState(CombatState.CombatResult.VICTORY, heroAttackResult, enemyAttackResult, gold, exp)
            }

            else -> CombatState(CombatState.CombatResult.CONTINUE, heroAttackResult, enemyAttackResult)
        }
    }

    /**
     * Hero attacks Enemy.
     * @param hero      The hero who is fighting
     * @param enemy     The enemy in the fight
     * @return AttackResult
     */
    fun heroAttack(hero: Hero, enemy: Enemy): AttackResult {
        val roll = Random.nextInt(1, 11)
        val damage = when {
            roll == 1 -> 0.0
            roll % 2 == 0 -> {
                val special = hero.attackPower * 3
                enemy.health -= special
                special
            }

            else -> {
                enemy.health -= hero.attackPower
                hero.attackPower
            }
        }
        return AttackResult(hero.name, enemy.name, damage, roll % 2 == 0)
    }

    /**
     * Enemy attacks Hero.
     * @param hero      The hero who is fighting
     * @param enemy     The enemy in the fight
     * @return AttackResult
     */
    fun enemyAttack(enemy: Enemy, hero: Hero): AttackResult {
        val roll = Random.nextInt(0, 10)
        val damage = when {
            roll < 1 -> 0.0
            roll < 3 && enemy.magicPower > 5 -> {
                val magic = enemy.magicPower.toDouble()
                enemy.magicPower -= 5
                hero.health -= magic
                magic
            }

            else -> {
                hero.health -= enemy.attackPower
                enemy.attackPower
            }
        }
        return AttackResult(enemy.name, hero.name, damage, false)
    }

    /**
     * Resolves an attack result, returns a String describing the outcome.
     * @param result    The AttackResult to resolve
     * @return String   A string describing the outcome.
     */
    fun resolve(result: AttackResult?): String {
        if (result == null)
            return ""

        if (result.damage <= 0) {
            return "${result.attacker} missed!"
        } else {
            val type = if (result.wasSpecial) "special" else "normal"
            return "${result.attacker} hits ${result.target} for ${result.damage.toInt()} HP ($type)"
        }
    }
}
