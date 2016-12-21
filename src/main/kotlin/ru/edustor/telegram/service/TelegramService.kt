package ru.edustor.telegram.service

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendPhoto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.edustor.telegram.model.AccountProfile
import ru.edustor.telegram.util.extension.getAsByteArray
import java.awt.image.BufferedImage

@Service
class TelegramService {

    @Autowired
    lateinit var bot: TelegramBot

    private val logger = LoggerFactory.getLogger(TelegramService::class.java)

    fun sendText(user: AccountProfile, text: String, disableNotification: Boolean = true) {
        user.telegramChatId?.let { cid ->
            bot.execute(SendMessage(cid, text).disableNotification(disableNotification))
        }
    }

    fun sendImage(user: AccountProfile, image: BufferedImage, caption: String, disableNotification: Boolean = true) {
        user.telegramChatId?.let { cid ->
            bot.execute(
                    SendPhoto(cid, image.getAsByteArray())
                            .caption(caption)
                            .disableNotification(disableNotification)
            )
        }
    }
}