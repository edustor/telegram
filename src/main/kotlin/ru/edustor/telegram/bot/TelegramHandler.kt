package ru.edustor.telegram.bot

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.AbstractSendRequest

interface TelegramHandler {
    val COMMAND: String
    fun process(msg: Message): AbstractSendRequest<*>?
}