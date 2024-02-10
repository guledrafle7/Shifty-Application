package com.example.shifty.activities.paystaff

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.shifty.R
import com.example.shifty.databinding.ActivityPayStaffBinding
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.model.Pay
import com.example.shifty.model.QrCode
import com.example.shifty.utils.Constant.Companion.currentDate
import com.example.shifty.utils.ProgressDialogUtil
import com.example.shifty.utils.isNetworkAvailable
import com.example.shifty.utils.showToast
import java.util.*

class PayStaffActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayStaffBinding

    private lateinit var qrCode: QrCode
    private var isCheckIn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPayStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setListener()
    }

    private fun initView() {
        getAllQrCodes(currentDate)

        binding.tvDate.text = currentDate
    }

    private fun setListener() {
        binding.apply {
            spEmployees.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    qrCode = parent.selectedItem as QrCode
                    tvDate.text = qrCode.date

                    when {
                        qrCode.checkIn == "1" && qrCode.checkOut == "0" -> {
                            tvCheckIn.setTextColor(resources.getColor(R.color.color_green))
                            tvCheckOut.setTextColor(resources.getColor(R.color.black))
                            tvCheckIn.text = "Yes"
                            tvCheckOut.text = "No"
                            btnPay.isEnabled = false
                        }
                        qrCode.checkIn == "1" && qrCode.checkOut == "1" -> {
                            tvCheckIn.setTextColor(resources.getColor(R.color.color_green))
                            tvCheckOut.setTextColor(resources.getColor(R.color.color_green))
                            tvCheckIn.text = "Yes"
                            tvCheckOut.text = "Yes"
                            btnPay.isEnabled = true
                        }
                        else -> {
                            tvCheckIn.setTextColor(resources.getColor(R.color.black))
                            tvCheckOut.setTextColor(resources.getColor(R.color.black))
                            tvCheckIn.text = "No"
                            tvCheckOut.text = "No"
                            btnPay.isEnabled = false
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            tvDate.setOnClickListener {
                val c = Calendar.getInstance()
                val dpd = DatePickerDialog(this@PayStaffActivity, { _, year, month, dayOfMonth ->
                    tvDate.text = "$dayOfMonth/${month+1}/$year"
                    getAllQrCodes(date = tvDate.text.toString())
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                dpd.show()
            }

            tvCheckIn.setOnClickListener {
                applyColors(colorOne = getColor(R.color.color_green), colorTwo = getColor(R.color.black))
                isCheckIn = true
            }

            tvCheckOut.setOnClickListener {
                applyColors(colorOne = getColor(R.color.black), colorTwo = getColor(R.color.color_green))
                isCheckIn = false
            }

            appbar.ivBack.setOnClickListener {
                onBackPressed()
            }

            btnPay.setOnClickListener {
                ProgressDialogUtil.showProgressDialog(context = this@PayStaffActivity,"Loading, please wait...", cancelable = false)
                val pay = Pay(uid = qrCode.uid, date = tvDate.text.toString(), earn = "20 £")
                DataBaseOperations.savePays(pay = pay,::getResult)
            }
        }
    }

    private fun getResult(isSaved: Boolean, message: String) {
        when {
            isSaved -> {
                showToast(message)
                onBackPressed()
                ProgressDialogUtil.dismissProgressDialog()
            }
            else -> {
                showToast(message)
                ProgressDialogUtil.dismissProgressDialog()
            }
        }
    }

    private fun getAllQrCodes(date: String) {
        ProgressDialogUtil.showProgressDialog(context = this,"Loading, please wait...", cancelable = false)
        if(isNetworkAvailable()){
            DataBaseOperations.getQrCodes(date = date) {
                if(it != null){
                    val adapter: ArrayAdapter<QrCode> = ArrayAdapter<QrCode>(this, R.layout.custom_spinner_country_code_textview, it)
                    binding.spEmployees.adapter = adapter
                    ProgressDialogUtil.dismissProgressDialog()
                } else {
                    showAlertDialog()
                    ProgressDialogUtil.dismissProgressDialog()
                }
            }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("There is no shift for employees on your selected date.")

        builder.setNegativeButton(android.R.string.yes) { dialog, _ ->
            getAllQrCodes(date = currentDate)
            dialog.dismiss()
        }

        builder.show()
    }

    private fun applyColors(colorOne: Int, colorTwo: Int){
        binding.apply {
            tvCheckIn.setTextColor(colorOne)
            tvCheckOut.setTextColor(colorTwo)
        }
    }
}