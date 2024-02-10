package com.example.shifty.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.shifty.utils.Constant.Companion.APP_CXT
import com.example.shifty.utils.Constant.Companion.APP_TAG
import java.util.*

@SuppressLint("LogNotTimber")
fun showLog(message: Any){
    Log.d(APP_TAG, message.toString())
}

fun showToast(message: Any){
    Toast.makeText(APP_CXT, message.toString(), Toast.LENGTH_SHORT).show()
}

fun hideKeyboard() {
    val imm: InputMethodManager? = APP_CXT?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun isNetworkAvailable(): Boolean {
    val manager = APP_CXT!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = manager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun isGPSEnabled(): Boolean {
    val locationManager = APP_CXT!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun textCopiedToClipboard(label: String, text: Any){
    val clipboardManager = APP_CXT?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, text.toString())
    clipboardManager.setPrimaryClip(clipData)
    showToast("$label successfully Copied to Clipboard")
}

inline fun <reified T> Activity.startActivity(finish: Boolean = true) {
    Intent(this, T::class.java).apply {
        startActivity(this)
        if (finish) {
            finish()
        }
    }
}

inline fun <reified T> Activity.startIntentActivity(finish: Boolean = true, intentKey: String, intentValue: String) {
    val intent = Intent(this, T::class.java)
    intent.apply {
        putExtra(intentKey, intentValue)
        startActivity(this)
        if (finish) {
            finish()
        }
    }
}