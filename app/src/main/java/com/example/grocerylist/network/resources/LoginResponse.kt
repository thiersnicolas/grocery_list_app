package com.example.grocerylist.model.network.rest.resources

import com.example.grocerylist.domain.AppUser
import com.squareup.moshi.Json
import java.util.*

data class LoginResponse(
    @Json(name = "token")
    var token: String,

    @Json(name = "user")
    var userData: UserData
) {
    fun mapToUser(): AppUser {
        return AppUser(
            UUID.fromString(this.userData.id),
            this.userData.name,
            this.userData.email
        )
    }
}

data class UserData(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "email")
    val email: String,
)
