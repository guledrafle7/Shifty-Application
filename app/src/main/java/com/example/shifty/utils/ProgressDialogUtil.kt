package com.example.shifty.utils

import android.app.ProgressDialog
import android.content.Context

object ProgressDialogUtil {

    var progressDialog: ProgressDialog? = null

    fun showProgressDialog(context: Context, message: String, cancelable : Boolean) {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setTitle("Loading")
        progressDialog!!.setMessage(message)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCancelable(cancelable)
        progressDialog!!.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
}