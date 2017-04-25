package ru.edustor.telegram.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AccountProfile() {
    @Id lateinit var accountId: String
    var telegramChatId: String? = null
    var telegramLinkToken: String? = null

    constructor(accountId: String) : this() {
        this.accountId = accountId
    }
}