package com.chrisyoung.auth

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam


@Controller
class LoginController {
    @GetMapping("/login")
    fun loginForm(model: Model): String {
        model["title"] = "Login"
        return "login"
    }
    @PostMapping("/login", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(
            model: Model,
            @RequestParam(name = "username") username: String,
            @RequestParam(name = "password") password: String
    ): String {
        model["title"] = "Login"
        model["username"] = username;
        model["password"] = password;
        return "loggedin"
    }
}

