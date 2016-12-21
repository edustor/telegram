package ru.edustor.telegram.bot.commands

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Component
import ru.edustor.commons.api.UploadApi
import ru.edustor.telegram.bot.TelegramHandler
import ru.edustor.telegram.repository.ProfileRepository
import ru.edustor.telegram.util.extension.cid
import ru.edustor.telegram.util.extension.replyText

@Component
open class NextUploadCommandHandler(val userRepository: ProfileRepository,
                                    val uploadApi: UploadApi) : TelegramHandler {
    override val COMMAND: String = "nu"

    companion object {
        val uuidRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}".toRegex()
    }

    override fun process(msg: Message): AbstractSendRequest<SendMessage>? {
        val user = userRepository.findByTelegramChatId(msg.cid()) ?: return msg.replyText("You're not logged in")

        val arg = msg.text().split(" ").getOrNull(1)
        val lessonId = arg?.let { uuidRegex.find(arg)?.value ?: return msg.replyText("Invalid URL/UUID") }

        val resp = uploadApi.setNextUploadTarget(user.accountId, lessonId).execute()
        return when (resp.code()) {
            204 -> msg.replyText("Upload server confirmed target override to $lessonId")
            else -> msg.replyText("Error: Upload server returned code ${resp.code()}")
        }
    }
}