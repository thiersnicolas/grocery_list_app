package com.example.grocerylist.network.resources

import com.example.grocerylist.domain.GroceryListProduct
import com.example.grocerylist.domain.User
import com.squareup.moshi.Json
import java.util.*

data class GroceryListProductsResponse(
    @Json(name = "products")
    val products: List<GroceryListProductData>
) {
    fun mapToGroceryListProducts(groceryListId: UUID): List<GroceryListProduct> {
        return this.products
            .map {
                it.mapToGroceryListProduct(groceryListId)
            }
    }
}

data class GroceryListProductData(
    @Json(name = "productId")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "amount")
    val amount: Int?,
    @Json(name = "unit")
    val unit: String?,
    @Json(name = "addedBy")
    val addedBy: GroceryListUserData,
    @Json(name = "remark")
    val remark: String?,
    @Json(name = "updatedBy")
    val updatedBy: GroceryListUserData?,
) {
    fun mapToGroceryListProduct(groceryListId: UUID): GroceryListProduct {
        return GroceryListProduct(
            UUID.fromString(this.id),
            groceryListId,
            name,
            amount,
            unit,
            User(
                UUID.fromString(this.addedBy.id),
                this.addedBy.name
            ),
            remark,
            this.updatedBy?.let { User(UUID.fromString(it.id), it.name) },
        )
    }
}