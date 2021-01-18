package com.chrisyoung.auth

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@Controller
class LoginController(private val userRepository: UserRepository) {
    @GetMapping("/login")
    fun loginForm(model: Model, request: HttpServletRequest): String {
        model["title"] = "Login"
        model["error"] = "";
        model["hasError"] = false;
        val session = request.session
        val user: User? = session.getAttribute("user") as User?
        if (user != null) {
            model["username"] = user.username
            model["password"] = user.password
            return "loggedin"
        }
        return "login"
    }
    @PostMapping("/login", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(
            model: Model,
            request: HttpServletRequest,
            @RequestParam(name = "username") username: String,
            @RequestParam(name = "password") password: String
    ): String {
        model["title"] = "Login"
        model["error"] = ""
        model["hasError"] = false
        model["username"] = username;
        model["password"] = password;
        val user = userRepository.findByUsername(username)
        if (user !== null && password == user.password) {
            val session = request.session
            session.setAttribute("username", username)
            session.setAttribute("user", user)
            return "loggedin"
        } else {
            model["hasError"] = true;
            model["error"] = "user not found or password incorrect"
        }
        return "login"
    }
}

