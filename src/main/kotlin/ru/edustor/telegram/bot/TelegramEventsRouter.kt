package ru.edustor.telegram.bot

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.GetFile
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service
import ru.edustor.commons.api.UploadApi
import ru.edustor.telegram.bot.TelegramHandler
import ru.edustor.telegram.repository.ProfileRepository
import ru.edustor.telegram.util.extension.cid
import ru.edustor.telegram.util.extension.replyText

@Service
open class TelegramEventsRouter(val bot: TelegramBot,
                                val profileRepository: ProfileRepository,
                                val uploadApi: UploadApi) {

    private val commandRegex = "/(\\w*)".toRegex()

    val handlers = mutableMapOf<String, TelegramHandler>()

    fun registerCommand(command: String, handler: TelegramHandler) {
        if (handlers.containsKey(command)) throw IllegalStateException("TelegramHandler $command redeclaration")
        handlers[command] = handler
    }

    fun processUpdate(update: Update) {
        val msg = update.message()
        if (msg != null) {
            if (msg.document() != null) {
                val fileId = msg.document().fileId()
                val file = bot.execute(GetFile(fileId)).file()
                val url = bot.getFullFilePath(file)

                val profile = profileRepository.findByTelegramChatId(msg.cid())
                if (profile == null) {
                    bot.execute(msg.replyText("You're not logged in"))
                    return
                }

                bot.execute(msg.replyText("Forwarding file to upload server..."))

                val resp = try {
                    uploadApi.uploadPdfByUrl(url, profile.accountId).execute()
                } catch (e: Exception) {
                    bot.execute(msg.replyText("Exception occurred: $e"))
                    return
                }

                when (resp.isSuccessful) {
                    true -> bot.execute(msg.replyText("Successfully uploaded. Id: ${resp.body().uuid.split("-").last()}"))
                    false -> bot.execute(msg.replyText("Something went wrong: upload server returned ${resp.code()}"))
                }
            } else if (msg.text() != null) {
                routeTextMessage(msg)
            }
        }
    }

    private fun routeTextMessage(msg: Message) {
        val text = msg.text()
        val command = commandRegex.find(text)?.groupValues?.get(1)

        if (command != null) {
            val handler = handlers[command]
            val resp: AbstractSendRequest<*>?

            resp = if (handler != null) {
                try {
                    handler.process(msg)
                } catch (e: Exception) {
                    msg.replyText("Exception occurred: $e")
                }
            } else {
                msg.replyText("Unsupported command")
            }
            resp?.let { bot.execute(it) }
        } else {
            bot.execute(SendMessage(msg.chat().id(), "Invalid message"))
        }
    }

}