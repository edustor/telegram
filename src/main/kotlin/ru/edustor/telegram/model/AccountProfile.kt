package ru.edustor.telegram.model

import ru.edustor.commons.models.internal.accounts.EdustorAccount
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Transient

@Entity
class AccountProfile() {
    @Id lateinit var accountId: String
    var telegramChatId: String? = null
    var telegramLinkToken: String? = null
    @Transient var account: EdustorAccount? = null

    constructor(accountId: String) : this() {
        this.accountId = accountId
    }
}