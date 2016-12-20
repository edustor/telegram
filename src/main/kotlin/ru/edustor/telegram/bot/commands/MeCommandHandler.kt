package ru.edustor.telegram.bot.commands

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Component
import ru.edustor.telegram.bot.TelegramHandler
import ru.edustor.telegram.repository.ProfileRepository
import ru.edustor.telegram.util.extension.cid
import ru.edustor.telegram.util.extension.replyText

@Component
open class MeCommandHandler(val userRepository: ProfileRepository) : TelegramHandler {
    override val COMMAND: String = "me"

    override fun process(msg: Message): AbstractSendRequest<SendMessage>? {
        val user = userRepository.findByTelegramChatId(msg.cid()) ?: return msg.replyText("You're not logged in")

        return msg.replyText("You're logged in as ${user.accountId}")
    }
}