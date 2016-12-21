package ru.edustor.telegram.rabbit

import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.*
import org.springframework.stereotype.Component
import ru.edustor.commons.models.internal.processing.pdf.PdfUploadedEvent
import ru.edustor.telegram.repository.ProfileRepository
import ru.edustor.telegram.repository.getForAccountId
import ru.edustor.telegram.service.TelegramService

@Component
open class PdfUploadListener(val profileRepository: ProfileRepository,
                             val telegramService: TelegramService) {
    @RabbitListener(bindings = arrayOf(QueueBinding(
            value = Queue("telegram.edustor/inbox/events/upload", durable = "true", arguments = arrayOf(
                    Argument(name = "x-dead-letter-exchange", value = "reject.edustor")
            )),
            exchange = Exchange("internal.edustor", type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true",
                    durable = "true"),
            key = "uploaded.pdf.pages.processing"
    )))
    fun processPdfUploadEvent(event: PdfUploadedEvent) {
        val shortUploadId = event.uuid.split("-").last()
        telegramService.sendText(profileRepository.getForAccountId(event.userId),
                "New upload received. Id: $shortUploadId. Target id: ${event.targetLessonId}. Timestamp: ${event.timestamp}")
    }
}