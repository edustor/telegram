package ru.edustor.telegram.rabbit

import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.*
import org.springframework.stereotype.Component
import ru.edustor.commons.models.internal.processing.pdf.PageProcessedEvent
import ru.edustor.commons.models.internal.processing.pdf.PdfUploadedEvent
import ru.edustor.telegram.repository.ProfileRepository
import ru.edustor.telegram.repository.getForAccountId
import ru.edustor.telegram.service.TelegramService

@Component
open class PdfProcessingListener(val profileRepository: ProfileRepository,
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
                "[NEW] Upload: $shortUploadId. Target id: ${event.targetLessonId}. Timestamp: ${event.timestamp}")
    }

    @RabbitListener(bindings = arrayOf(QueueBinding(
            value = Queue("telegram.edustor/inbox/events/finished", durable = "true", arguments = arrayOf(
                    Argument(name = "x-dead-letter-exchange", value = "reject.edustor")
            )),
            exchange = Exchange("internal.edustor", type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true",
                    durable = "true"),
            key = "finished.pages.processing"
    )))
    fun processPageProcessedEvent(event: PageProcessedEvent) {
        val shortUploadId = event.uploadUuid.split("-").last()
        val shortQrId = event.qrUuid?.split("-")?.last()

        val accountProfile = profileRepository.getForAccountId(event.userId)
        when (event.success) {
            true -> telegramService.sendText(accountProfile,
                    "[OK] Upload: $shortUploadId. Page ${event.pageIndex.plus(1)} of ${event.totalPageCount}. " +
                            "QR: $shortQrId. Target: ${event.targetLessonName}")
            false -> telegramService.sendText(accountProfile,
                    "[FAIL] Upload: $shortUploadId. Page ${event.pageIndex.plus(1)} of ${event.totalPageCount}. " +
                            "QR: $shortQrId.")
        }
    }
}