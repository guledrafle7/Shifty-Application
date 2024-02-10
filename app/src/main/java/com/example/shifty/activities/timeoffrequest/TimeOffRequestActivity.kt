package com.example.shifty.activities.timeoffrequest

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shifty.databinding.ActivityTimeOffRequestBinding
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.model.TimeOff
import com.example.shifty.utils.Constant.Companion.currentUser
import com.example.shifty.utils.ProgressDialogUtil
import com.example.shifty.utils.showLog
import java.util.*

class TimeOffRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimeOffRequestBinding

    private var startDate: String? = null
    private var endDate: String? = null
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTimeOffRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(currentUser != null){
            email = currentUser.email.toString()
        }

        val c = Calendar.getInstance()
        val yy = c.get(Calendar.YEAR)
        val mm = c.get(Calendar.MONTH)
        val dd = c.get(Calendar.DAY_OF_MONTH)

        binding.apply {
            tvDateStart.setOnClickListener {
                val dpd = DatePickerDialog(this@TimeOffRequestActivity, { _, year, month, dayOfMonth ->
                    tvDateStart.text = "$dayOfMonth/${month+1}/$year"
                    startDate = "$year${month+1}$dayOfMonth"
                }, yy, mm, dd)
                dpd.show()
            }

            tvDateEnd.setOnClickListener {
                val dpd = DatePickerDialog(this@TimeOffRequestActivity, { _, year, month, dayOfMonth ->
                    tvDateEnd.text = "$dayOfMonth/${month+1}/$year"
                    endDate = "$year${month+1}$dayOfMonth"
                }, yy, mm, dd)
                dpd.show()
            }

            tvRequest.setOnClickListener {
                ProgressDialogUtil.showProgressDialog(context = this@TimeOffRequestActivity,"Loading, please wait...", cancelable = false)
                val timeOff = TimeOff(uid = currentUser!!.uid, email = currentUser.email.toString(), startDate = startDate.toString(), endDate = endDate.toString(), isAllowed = "0")
                DataBaseOperations.saveTimeOff(timeOff,::onResult)
            }

            appbar.ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun onResult(result: Boolean, message: String) {
        if(result) {
            showLog(message)
            onBackPressed()
        } else
            showLog(message)
    }
}