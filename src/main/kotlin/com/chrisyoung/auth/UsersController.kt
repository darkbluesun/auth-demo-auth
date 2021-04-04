package com.chrisyoung.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class UserCreateBody(
    val email: String,
    val mobile: String,
    val firstName: String,
    val lastName: String
)

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class UsersController(
    val userRepository: UserRepository,
    val jwtService: JwtService
    ) {
    @GetMapping("/users")
    fun index(
        @RequestHeader(name = "authorization") auth: String
    ): ResponseEntity<Any> {
        val token = auth.replace("Bearer ", "")
        jwtService.verify(token);
        val users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }
    @GetMapping("/users/{id}")
    fun getUser(
        @PathVariable id: Long,
        @RequestHeader(name = "authorization") auth: String
    ): ResponseEntity<Any> {
        val token = auth.replace("Bearer ", "")
        jwtService.verify(token);
        val user = userRepository.findById(id);
        return ResponseEntity.ok().body(user);
    }
    @PostMapping("/users")
    fun add(
        @RequestHeader(name = "authorization") auth: String,
        @RequestBody(required = true) requestBody: UserCreateBody
    ): ResponseEntity<Any> {
        val token = auth.replace("Bearer ", "")
        jwtService.verify(token)
        val password = "password";
        val user = userRepository.save(User(
            requestBody.email,
            requestBody.mobile,
            requestBody.firstName,
            requestBody.lastName,
            password
        ))
        return ResponseEntity.ok(user);
    }
}