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
open class StartCommandHandler(val profileRepository: ProfileRepository) : TelegramHandler {
    override val COMMAND: String = "start"

    val tokenRegex = "/start ([\\w-]*)".toRegex()

    override fun process(msg: Message): AbstractSendRequest<SendMessage>? {
        val token = tokenRegex.find(msg.text())?.groupValues?.get(1) ?: return msg.replyText("Cannot find initialization token")
        val profile = profileRepository.findByTelegramLinkToken(token) ?: return msg.replyText("Unknown initialization token")

        profile.telegramChatId = msg.cid()
        profile.telegramLinkToken = null

        profileRepository.save(profile)

        return msg.replyText("Greetings, ${profile.accountId}. Now your Edustor account is linked with this Telegram account.")
    }
}