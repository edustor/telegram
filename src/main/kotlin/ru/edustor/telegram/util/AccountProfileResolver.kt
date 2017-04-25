package ru.edustor.telegram.util

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import ru.edustor.commons.auth.EdustorTokenValidator
import ru.edustor.commons.auth.internal.EdustorAuthProfileResolver
import ru.edustor.commons.auth.model.EdustorAuthProfile
import ru.edustor.telegram.model.AccountProfile
import ru.edustor.telegram.repository.ProfileRepository
import ru.edustor.telegram.repository.getForAccountId

open class AccountProfileResolver(val repo: ProfileRepository,
                                  validator: EdustorTokenValidator) : EdustorAuthProfileResolver(validator) {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == AccountProfile::class.java
    }

    override fun resolveArgument(parameter: MethodParameter?, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val authProfile = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory) as EdustorAuthProfile
        val profile = repo.getForAccountId(authProfile.accountId)

        return profile
    }
}