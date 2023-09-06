package com.example.grocerylist.network.rest.resources

import com.example.grocerylist.domain.GroceryList
import com.squareup.moshi.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class GroceryListsResponse(
    @Json(name = "data")
    var groceryLists: List<GroceryListData>,
) {
    fun mapToGroceryLists(): List<GroceryList> {
        return this.groceryLists
            .map {
                GroceryList(
                    UUID.fromString(it.id),
                    it.name,
                    UUID.fromString(it.createdBy),
                    LocalDateTime.parse(it.createdDate, DateTimeFormatter.ISO_DATE_TIME),
                    it.status
                )
            }
    }
}

data class GroceryListData(
    @Json(name = "groceryListId")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "createdBy")
    val createdBy: String,
    @Json(name = "createdDate")
    val createdDate: String,
    @Json(name = "status")
    val status: String,
)
