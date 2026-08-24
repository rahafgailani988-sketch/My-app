package com.example.model

data class UserProfile(
    val fullName: String = "",
    val phoneNumber: String = "",
    val country: String = "",
    val city: String = "",
    val preciseLocation: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isRegistered: Boolean = false
) {
    val isComplete: Boolean
        get() = fullName.isNotBlank() &&
                phoneNumber.isNotBlank() &&
                country.isNotBlank() &&
                city.isNotBlank() &&
                preciseLocation.isNotBlank()
}
