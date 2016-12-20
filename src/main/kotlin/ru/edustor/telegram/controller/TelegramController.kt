package ru.edustor.telegram.controller

import com.pengrad.telegrambot.BotUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.edustor.commons.exceptions.http.NotFoundException
import ru.edustor.telegram.bot.TelegramEventsRouter
import ru.edustor.telegram.config.TelegramConfig

@Controller
class TelegramController(val telegramEventsRouter: TelegramEventsRouter, val telegramConfig: TelegramConfig) {
    @RequestMapping("/telegram/{token}")
    fun processUpdates(@PathVariable token: String, @RequestBody(required = false) body: String?): ResponseEntity<String> {
        val actualToken = telegramConfig.telegramToken

        if (token == actualToken && body != null) {
            val update = BotUtils.parseUpdate(body)
            telegramEventsRouter.processUpdate(update)
            val headers = HttpHeaders()
            headers[HttpHeaders.CONTENT_TYPE] = "application/json"
            return ResponseEntity("{}", headers, HttpStatus.OK)
        } else {
            throw NotFoundException()
        }
    }
}