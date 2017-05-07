package ru.edustor.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.edustor.telegram.model.AccountProfile

@Repository
interface ProfileRepository : JpaRepository<AccountProfile, String> {
    fun findByTelegramLinkToken(token: String): AccountProfile?
    fun findByTelegramChatId(token: String): AccountProfile?
}

fun ProfileRepository.getForAccountId(id: String): AccountProfile {
    return this.findOne(id) ?: let {
        val a = AccountProfile(id)
        this.save(a)
        return@let a
    }
}