package com.chrisyoung.auth

import javassist.NotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.lang.String.format
import javax.persistence.EntityManager
import javax.servlet.http.HttpServletRequest

@Controller
class AuthController(
        private val entityManager: EntityManager,
        private val clientRepository: ClientRepository,
        private val codeRepository: CodeRepository
) {
    @GetMapping("/authorize")
    fun authorizeForm(
            model: Model,
            @RequestParam(name = "state") state: String,
            @RequestParam(name = "clientId") clientId: Long
    ): String {
        val client = clientRepository.findById(clientId).orElse(null)
        client ?: return ":notfound"
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
        client ?: return "notfound:"
        val user = request.session.getAttribute("user") as User? ?: return "redirect:/login"
        val code = codeRepository.save(Code(client.secret, client, user))

        model["title"] = "Authorized"
        model["redirectUrl"] = format("%s?state=%s&code=%s", client.redirectUrl, state, code.code)
        return "authorize"
    }

}