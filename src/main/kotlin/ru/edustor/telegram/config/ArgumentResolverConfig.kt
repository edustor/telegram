package ru.edustor.telegram.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import ru.edustor.commons.auth.EdustorTokenValidator
import ru.edustor.telegram.util.AccountProfileResolver
import ru.edustor.telegram.repository.ProfileRepository

@Configuration
open class ArgumentResolverConfig(val repo: ProfileRepository,
                                  val validator: EdustorTokenValidator) : WebMvcConfigurerAdapter() {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        val resolver = AccountProfileResolver(repo, validator)
        argumentResolvers.add(resolver)
    }
}