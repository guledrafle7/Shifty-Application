package com.example.shifty.model

data class TimeOff(
    val uid: String,
    val email: String,
    val startDate: String,
    val endDate: String,
    val isAllowed: String,
) {
    override fun toString(): String {
        return email
    }
}