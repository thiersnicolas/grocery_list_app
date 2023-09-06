package com.example.grocerylist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.grocerylist.domain.AppUser
import java.util.*

@Entity(tableName = "user")
data class AppUserDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id")
    val id: UUID,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
)

fun AppUserDB.asDomainModel(): AppUser {
    return AppUser(
        id = id,
        name = name,
        email = email
    )
}