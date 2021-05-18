package com.chrisyoung.auth.controller

import com.chrisyoung.auth.User
import com.chrisyoung.auth.UserRepository
import org.springframework.http.HttpStatus
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
class UsersController(val userRepository: UserRepository) {
    @GetMapping("/users")
    fun index(): ResponseEntity<Any> {
        val users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }
    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<Any> {
        val user = userRepository.findById(id);
        return ResponseEntity.ok().body(user);
    }
    @PostMapping("/users")
    fun add(@RequestBody(required = true) requestBody: UserCreateBody): ResponseEntity<User> {
        val password = "password";
        val user = userRepository.save(
            User(
            requestBody.email,
            requestBody.mobile,
            requestBody.firstName,
            requestBody.lastName,
            password
        )
        )
        return ResponseEntity(user, HttpStatus.CREATED);
    }
}