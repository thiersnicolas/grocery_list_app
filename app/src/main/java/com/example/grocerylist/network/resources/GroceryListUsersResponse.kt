package com.example.grocerylist.network.resources

import com.example.grocerylist.domain.GroceryListUser
import com.example.grocerylist.domain.User
import com.squareup.moshi.Json
import java.util.*

data class GroceryListUsersResponse(
    @Json(name = "data")
    val data: GroceryListUsersData
) {
    fun mapToGroceryListUsers(groceryListId: UUID): List<GroceryListUser> {
        return this.data.groceryListUsers
            .map {
                GroceryListUser(
                    User(
                        UUID.fromString(it.id),
                        it.name
                    ),
                    groceryListId
                )
            }
    }
}

data class GroceryListUsersData(
    @Json(name = "groceryListUsers")
    val groceryListUsers: List<GroceryListUserData>
)