package com.example.shifty.activities.empshift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shifty.databinding.ActivityEmpShiftBinding
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.model.Shift
import com.example.shifty.utils.Constant
import com.example.shifty.utils.Constant.Companion.currentDate

class EmpShiftActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmpShiftBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEmpShiftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbar.ivBack.setOnClickListener {
            onBackPressed()
        }

        val date = intent.getStringExtra("date").toString()
        val timeSlot = intent.getStringExtra("timeSlot").toString()
        val parts = date.split("/")
        binding.apply {
            tvBookShift.text = timeSlot
            datePicker.updateDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
        }
    }
}