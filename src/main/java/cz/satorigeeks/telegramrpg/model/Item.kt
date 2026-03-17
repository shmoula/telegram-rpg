package cz.satorigeeks.telegramrpg.model

enum class ItemType {
    POTION,
    WEAPON
}

data class Item(
    val name: String,
    val type: ItemType,
    var healingPower: Int,
    var attackPower: Int,
    var cost: Int,
    var isEquipped: Boolean = false
)
