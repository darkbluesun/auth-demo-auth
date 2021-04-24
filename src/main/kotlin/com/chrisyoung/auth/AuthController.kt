package com.chrisyoung.auth

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import java.lang.String.format
import javax.persistence.EntityManager
import javax.servlet.http.HttpServletRequest

@Controller
class AuthController(
        private val entityManager: EntityManager,
        private val clientRepository: ClientRepository,
        private val codeRepository: CodeRepository,
        private val userRepository: UserRepository
) {
    private val chars = ('0'..'z').toList().toTypedArray()

    @GetMapping("/authorize")
    fun authorizeForm(
            model: Model,
            request: HttpServletRequest,
            @RequestParam(name = "state") state: String,
            @RequestParam(name = "clientId") clientId: Long
    ): String {
        val client = clientRepository.findById(clientId).orElse(null)
        client ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "client not found")
        model["title"] = "Authorize"
        model["clientId"] = clientId
        model["clientName"] = client.name
        model["state"] = state
        return "authorizeForm"
    }
    @PostMapping("/authorize")
    fun authorize(
            model: Model,
            request: HttpServletRequest,
            @RequestParam(name = "state") state: String,
            @RequestParam(name = "clientId") clientId: Long
    ): String {
        val client: Client? = clientRepository.findById(clientId).orElse(null)
        client ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "client not found")
        val user = userRepository.findByEmail(request.userPrincipal.name) ?:
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        val secret = (1..32).map { chars.random() }.joinToString("")
        val code = codeRepository.save(Code(secret, client, user))

        model["title"] = "Authorized"
        val redirectUrl = format("%s?state=%s&code=%s", client.redirectUrl, state, code.code)
        return "redirect:$redirectUrl"
    }

}