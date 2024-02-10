package com.example.shifty.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.shifty.MyApplication
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class Constant {
    companion object{

        @SuppressLint("StaticFieldLeak")
        val APP_CXT = MyApplication.appContext

        const val APP_TAG = "SHIFTY_TAG"

        const val EMPLOYEE = 1
        const val MANAGER = 2
        const val PAYROLL = 3

        const val QR_GENERATED_VALUE_KEY = "qrGeneratedValue"
        const val QR_GENERATED_DATE_KEY = "qrGeneratedDate"

        private const val MAP_SHARED_PREF = "mapSharedPref"
        const val IS_FIRST_KEY = "isFirst"
        const val LANGUAGE_KEY = "lng"
        const val CLICKED_KEY = "clicked"

        var sharedPreferences = APP_CXT!!.getSharedPreferences(MAP_SHARED_PREF, Context.MODE_PRIVATE)!!
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
        const val EMAIL_KEY = "email"

        var currentDate: String = SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).format(Date())
        var date: String = SimpleDateFormat("yyyyMdd", Locale.getDefault()).format(Date())

        val currentUser= FirebaseAuth.getInstance().currentUser
    }
}