package com.example.shifty.activities.employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.activities.authentication.LoginActivity
import com.example.shifty.activities.empshift.EmpShiftActivity
import com.example.shifty.activities.paid.PaidListActivity
import com.example.shifty.activities.timeoffrequest.TimeOffRequestActivity
import com.example.shifty.databinding.ActivityEmployeeBinding
import com.example.shifty.model.QrCode
import com.example.shifty.model.Shift
import com.example.shifty.utils.Constant.Companion.currentDate
import com.example.shifty.utils.Constant.Companion.currentUser
import com.example.shifty.utils.showToast
import com.example.shifty.utils.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class EmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeBinding

    private var shift: Shift? = null
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(currentUser != null) {
            DataBaseOperations.getEmployeeShift(currentUser.uid,::getShift)
            email = FirebaseAuth.getInstance().currentUser!!.email.toString()
        }

        setListener()

        binding.tvNextPayDate.text = currentDate
    }

    private fun setListener() {
        binding.apply {
            imgClose.setOnClickListener {
                closeDrawer()
            }

            imgMenu.setOnClickListener {
                openDrawer()
            }

            btnLogout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@EmployeeActivity, LoginActivity::class.java))
                finish()
            }

            cardClockInOut.setOnClickListener {
                scanQr()
            }

            llEmpShift.setOnClickListener {
                val intent = Intent(this@EmployeeActivity, EmpShiftActivity::class.java)
                if(shift != null) {
                    intent.apply {
                        putExtra("date", shift!!.date)
                        putExtra("timeSlot", shift!!.timeSlot)
                    }
                } else {
                    intent.apply {
                        putExtra("date", currentDate)
                        putExtra("timeSlot", "None")
                    }
                }
                startActivity(intent)
            }

            llTimeOffRequest.setOnClickListener {
                startActivity<TimeOffRequestActivity>(finish = false)
            }

            llNextPayDate.setOnClickListener {
                startActivity<PaidListActivity>(finish = false)
            }
        }
    }

    private fun getShift(shift: Shift?) {
        if(shift != null) {
            val date = shift.date
            val timeSlot = shift.timeSlot
            binding.apply {
                tvShiftTime.text = timeSlot
                tvShiftDate.text = date
            }

            this.shift = shift
        }
    }

    private fun scanQr() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0)
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        if (result.contents == null) {
            showMessage("Error Failed To Scan QR")
        } else {
            val strResult = result.contents
            showMessage("QR CONTENT : $strResult")

            val str = strResult.split(",").toTypedArray()

            if(email != null)
                saveQr(QrCode(uid = FirebaseAuth.getInstance().uid!!, email = email.toString(), shift = str[0], date = currentDate, checkIn = str[1], checkOut = str[2]))
            else
                showToast("Something goes wrong, refresh App please")
        }
    }

    private fun saveQr(qrCode: QrCode) {
        showMessage("Saving Qr Code...")
        DataBaseOperations.saveQrCode(qrCode,::onResult)
    }

    private fun onResult(error: Boolean, message: String) {
        showMessage(message)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    private fun closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START, true)
        }
    }

    private fun openDrawer() {
        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}