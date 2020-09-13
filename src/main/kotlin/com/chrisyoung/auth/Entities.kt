package com.chrisyoung.auth

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class User(
        var username: String,
        var password: String,
        @Id @GeneratedValue var id: Long? = null)

@Entity
class Client(
        var name: String,
        var secret: String,
        var redirectUrl: String,
        @Id @GeneratedValue var id: Long? = null)

@Entity
class Code(
        var code: String,
        @ManyToOne var client: Client,
        @ManyToOne var user: User,
        @Id @GeneratedValue var id: Long? = null)