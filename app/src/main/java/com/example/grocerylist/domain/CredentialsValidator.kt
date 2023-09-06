package com.example.grocerylist.domain

class CredentialsValidator {

    companion object {
        fun validateName(name: String?): Boolean {
            if (name == null || name.trim { it <= ' ' }
                    .isEmpty() || name.isEmpty() || name.length <= 5
            ) {
                return false
            }
            return true
        }

        fun validateEmail(email: String?): Boolean {
            if (email == null || email.trim { it <= ' ' }
                .isEmpty() || email.isEmpty() || email.length <= 5
            ) {
                return false
            }
            return true
        }

        fun validatePassword(password: String?): Boolean {
            if (password == null || password.trim { it <= ' ' }
                .isEmpty() || password.length <= 4
            ) {
                return false
            }
            return true
        }
    }
}
