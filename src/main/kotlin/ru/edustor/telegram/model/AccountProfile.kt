package ru.edustor.telegram.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import ru.edustor.commons.models.internal.accounts.EdustorAccount

@Document
class AccountProfile() {
    @Id lateinit var accountId: String
    var telegramChatId: String? = null
    var telegramLinkToken: String? = null
    @Transient var account: EdustorAccount? = null

    constructor(accountId: String) : this() {
        this.accountId = accountId
    }
}