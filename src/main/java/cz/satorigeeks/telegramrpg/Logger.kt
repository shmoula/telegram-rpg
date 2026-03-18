package cz.satorigeeks.telegramrpg

import eu.vendeli.tgbot.types.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Logger {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    fun log(user: User, action: String, detail: String = "") {
        val time = LocalDateTime.now().format(formatter)
        val name = user.username?.let { "@$it" } ?: user.firstName
        val detailPart = if (detail.isNotEmpty()) " | $detail" else ""
        println("[$time] $name (#${user.id}) > $action$detailPart")
    }
}
