package ru.edustor.telegram.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edustor.commons.auth.annotation.RequiresAuth
import ru.edustor.telegram.model.AccountProfile
import ru.edustor.telegram.repository.ProfileRepository
import java.util.*

@RestController
@RequestMapping("/api/v1")
class LinkRestController(val profileRepository: ProfileRepository) {

    @RequestMapping("link")
    @RequiresAuth("interactive")
    fun getTelegramLink(profile: AccountProfile): String {

        val token = UUID.randomUUID().toString()
        profile.telegramLinkToken = token
        profileRepository.save(profile)

        return "https://telegram.me/edustor_bot?start=$token"
    }
}