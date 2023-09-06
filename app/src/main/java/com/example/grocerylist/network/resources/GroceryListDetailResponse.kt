package com.example.grocerylist.network.resources

import com.example.grocerylist.domain.GroceryListDetail
import com.example.grocerylist.domain.User
import com.squareup.moshi.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


data class GroceryListDetailResponse(
    @Json(name = "groceryListId")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "createdBy")
    val createdBy: GroceryListUserData,
    @Json(name = "createdDate")
    val createdDate: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "collectBy")
    val collectBy: GroceryListUserData?,
    @Json(name = "currency")
    val currency: String?,
    @Json(name = "price")
    val price: Int?,
) {
    fun mapToGroceryListDetail(): GroceryListDetail {
        return GroceryListDetail(
            UUID.fromString(this.id),
            this.name,
            User(
                UUID.fromString(this.createdBy.id),
                this.createdBy.name
            ),
            LocalDateTime.parse(this.createdDate, DateTimeFormatter.ISO_DATE_TIME),
            this.status,
            this.collectBy?.let { User(UUID.fromString(it.id), it.name) },
            this.currency,
            this.price
        )
    }
}