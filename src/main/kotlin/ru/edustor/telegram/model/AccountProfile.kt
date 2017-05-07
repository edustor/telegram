package ru.edustor.telegram.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "account_profiles")
class AccountProfile() {
    @Id lateinit var accountId: String
    var telegramChatId: String? = null
    var telegramLinkToken: String? = null

    constructor(accountId: String) : this() {
        this.accountId = accountId
    }
}