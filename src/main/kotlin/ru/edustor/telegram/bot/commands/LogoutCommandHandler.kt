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
open class LogoutCommandHandler(val profileRepository: ProfileRepository) : TelegramHandler {

    override val COMMAND: String = "logout"

    override fun process(msg: Message): AbstractSendRequest<SendMessage>? {
        val profile = profileRepository.findByTelegramChatId(msg.cid()) ?: return msg.replyText("You're not logged in")
        profile.telegramChatId = null
        profileRepository.save(profile)
        return msg.replyText("Logged out")
    }
}