package com.example.shifty.model

data class QrCode(
    val uid:String,
    val email:String,
    val shift:String,
    val date:String,
    val checkIn:String,
    val checkOut:String,
    ) {
    override fun toString(): String {
        return email
    }
}