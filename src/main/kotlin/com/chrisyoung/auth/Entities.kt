package com.chrisyoung.auth

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
        var username: String,
        var password: String,
        @Id @GeneratedValue var id: Long? = null)