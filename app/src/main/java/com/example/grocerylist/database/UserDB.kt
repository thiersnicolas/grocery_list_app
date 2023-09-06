package com.example.grocerylist.database

import com.example.grocerylist.domain.User
import java.util.*

class UserDB {
    companion object {
        fun toUser(
            collectByUserId: UUID?,
            collectByUserName: String?
        ): User? {
            return if (collectByUserId != null && collectByUserName != null) {
                User(collectByUserId, collectByUserName)
            } else null
        }
    }
}
