package ru.edustor.telegram.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edustor.commons.auth.assertScopeContains
import ru.edustor.telegram.model.AccountProfile
import ru.edustor.telegram.repository.ProfileRepository
import java.util.*

@RestController
@RequestMapping("/api/v1")
class LinkRestController(val profileRepository: ProfileRepository) {

    @RequestMapping("link")
    fun getTelegramLink(profile: AccountProfile): String {
        profile.account!!.assertScopeContains("interactive")

        val token = UUID.randomUUID().toString()
        profile.telegramLinkToken = token
        profileRepository.save(profile)

        return "https://telegram.me/edustor_bot?start=$token"
    }
}