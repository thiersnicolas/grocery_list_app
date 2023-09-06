package com.example.jokeapp.screens.BindingUtils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.grocerylist.R
import com.example.grocerylist.domain.AppUser
import com.example.grocerylist.domain.GroceryListDetail
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@BindingAdapter("ownerImage")
fun ImageView.setOwnerImage(isOwner: Boolean) {
    if (isOwner) {
        setImageResource(R.drawable.ic_check_icon)
    } else {
        setImageResource(R.drawable.ic_uncheck_icon)
    }
}

@BindingAdapter("dateFormatted")
fun TextView.setDateFormatted(date: LocalDateTime?) {
    if (date == null) {
        text = ""
    } else {
        text = date.format(
            DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.MEDIUM,
                FormatStyle.SHORT
            )
        )
    }
}

@BindingAdapter("groceryListCreatedBy")
fun TextView.setGroceryListCreatedBy(groceryListDetail: GroceryListDetail?) {
    if (groceryListDetail?.createdBy == null) {
        text = ""
    } else {
        text = "created by: ${groceryListDetail.createdBy.name}"
    }
}

@BindingAdapter("groceryListPrice")
fun TextView.setGroceryListPrice(groceryListDetail: GroceryListDetail?) {
    if (groceryListDetail?.price == null) {
        text = ""
    } else {
        val currency = if (groceryListDetail.currency != null) {
            " " + groceryListDetail.currency
        } else {
            ""
        }
        text = "price: ${groceryListDetail.price}$currency"
    }
}

@BindingAdapter("groceryListStatus")
fun TextView.setGroceryListStatus(groceryListDetail: GroceryListDetail?) {
    if (groceryListDetail?.status == null) {
        text = ""
    } else {
        text = "status: ${groceryListDetail.status}"
    }
}


@BindingAdapter("groceryListCollectBy")
fun TextView.setGroceryListCollectBy(groceryListDetail: GroceryListDetail?) {
    if (groceryListDetail?.collectBy == null) {
        text = ""
    } else {
        text = "collect by: ${groceryListDetail.collectBy.name}"
    }
}

@BindingAdapter("welcomeUser")
fun TextView.setWelcomeUser(appUser: AppUser?) {
    if (appUser == null) {
        text = ""
    } else {
        text = "Welcome ${appUser.name}"
    }
}

@BindingAdapter("nameErrorMessage")
fun TextInputLayout.setNameErrorMessage(displayError: Boolean?) {
    if (displayError == null || !displayError) {
        error = ""
    } else {
        error = "Grocery List name is invalid"
    }
}

