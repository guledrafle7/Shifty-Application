package com.example.shifty.activities.bookshifts

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.shifty.R
import com.example.shifty.databinding.ActivityBookShiftBinding
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.model.Shift
import com.example.shifty.model.TimeOff
import com.example.shifty.model.Users
import com.example.shifty.utils.ProgressDialogUtil
import com.example.shifty.utils.isNetworkAvailable
import com.example.shifty.utils.showToast
import java.sql.Time
import java.util.*

class BookShiftActivity : AppCompatActivity() {

    private var shiftText: String? = null
    private lateinit var timeOff: TimeOff

    private lateinit var binding: ActivityBookShiftBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookShiftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setListener()
    }

    private fun initView() {
        if(isNetworkAvailable()) {
            ProgressDialogUtil.showProgressDialog(context = this,"Loading, please wait...", cancelable = false)
            DataBaseOperations.getAllEmployees(::getUserList)
        }

        shiftText = binding.tv9amTo5pm.text.toString().trim()
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
                    timeOff = parent.selectedItem as TimeOff
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            tv9amTo5pm.setOnClickListener {
                shiftText = tv9amTo5pm.text.toString().trim()
                applyColors(colorOne = getColor(R.color.color_green), colorTwo = getColor(R.color.black), colorThree = getColor(R.color.black))
            }

            tv5pmTo1am.setOnClickListener {
                shiftText = tv5pmTo1am.text.toString().trim()
                applyColors(colorOne = getColor(R.color.black), colorTwo = getColor(R.color.color_green), colorThree = getColor(R.color.black))
            }

            tv1amTo9am.setOnClickListener {
                shiftText = tv1amTo9am.text.toString().trim()
                applyColors(colorOne = getColor(R.color.black), colorTwo = getColor(R.color.black), colorThree = getColor(R.color.color_green))
            }

            tvShift.setOnClickListener {
                ProgressDialogUtil.showProgressDialog(context = this@BookShiftActivity,"Loading, please wait...", cancelable = false)
                val shift =Shift(uid = timeOff.uid, timeSlot = shiftText.toString(), date = getSelectedDatePicker())
                DataBaseOperations.saveShifts(shift,::onResult)
            }

            appbar.ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun onResult(isSaved: Boolean, message: String) {
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

    private fun getUserList(timeOffs: ArrayList<TimeOff>?) {
        if(timeOffs != null){
            val adapter: ArrayAdapter<TimeOff> = ArrayAdapter<TimeOff>(this, R.layout.custom_spinner_country_code_textview, timeOffs)
            binding.spEmployees.adapter = adapter
            ProgressDialogUtil.dismissProgressDialog()
        }
    }

    private fun getSelectedDatePicker() : String {
        val date: String
        binding.datePicker.apply {
            val day: Int = this.dayOfMonth
            val month: Int = this.month + 1
            val year: Int = this.year

            date = "$day/$month/$year"
        }
        return date
    }

    private fun applyColors(colorOne: Int, colorTwo: Int, colorThree: Int){
        binding.apply {
            tv9amTo5pm.setTextColor(colorOne)
            tv5pmTo1am.setTextColor(colorTwo)
            tv1amTo9am.setTextColor(colorThree)
        }
    }
}