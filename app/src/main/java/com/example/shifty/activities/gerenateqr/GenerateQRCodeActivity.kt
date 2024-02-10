package com.example.shifty.activities.gerenateqr

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shifty.R
import com.example.shifty.activities.generatedqr.GeneratedQRCodesActivity
import com.example.shifty.databinding.ActivityGenerateQrcodeBinding
import com.example.shifty.utils.Constant.Companion.QR_GENERATED_DATE_KEY
import com.example.shifty.utils.Constant.Companion.QR_GENERATED_VALUE_KEY
import com.example.shifty.utils.Constant.Companion.currentDate
import com.example.shifty.utils.startIntentActivity
import java.util.*

class GenerateQRCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateQrcodeBinding

    private var shiftText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGenerateQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setListener()
    }

    private fun initView() {
        binding.apply {
            tvDate.text = currentDate

            shiftText = tv9amTo5pm.text.toString().trim()
        }
    }

    private fun setListener() {
        binding.apply {
            tvDate.setOnClickListener {
                val c = Calendar.getInstance()
                val dpd = DatePickerDialog(this@GenerateQRCodeActivity, { _, year, month, dayOfMonth ->
                    tvDate.text = "$dayOfMonth/${month+1}/$year"
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                dpd.show()
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

            tvGenerateQR.setOnClickListener {
                val intent = Intent(applicationContext, GeneratedQRCodesActivity::class.java)
                intent.apply {
                    putExtra(QR_GENERATED_VALUE_KEY, shiftText)
                    putExtra(QR_GENERATED_DATE_KEY, tvDate.text.toString())
                }
                startActivity(intent)
            }

            appbar.ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun applyColors(colorOne: Int, colorTwo: Int, colorThree: Int){
        binding.apply {
            tv9amTo5pm.setTextColor(colorOne)
            tv5pmTo1am.setTextColor(colorTwo)
            tv1amTo9am.setTextColor(colorThree)
        }
    }
}