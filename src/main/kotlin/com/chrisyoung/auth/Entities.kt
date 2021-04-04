package com.chrisyoung.auth

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class User(
        val email: String,
        val mobile: String,
        val firstName: String,
        val lastName: String,
        val password: String,
        @Id @GeneratedValue val id: Long? = null)

@Entity
class Client(
        val name: String,
        val secret: String,
        val redirectUrl: String,
        @Id @GeneratedValue val id: Long? = null)

@Entity
class Code(
        val code: String,
        @ManyToOne val client: Client,
        @ManyToOne val user: User,
        @Id @GeneratedValue val id: Long? = null)