package com.example.shifty.activities.payroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.example.shifty.activities.authentication.LoginActivity
import com.example.shifty.activities.gerenateqr.GenerateQRCodeActivity
import com.example.shifty.activities.paystaff.PayStaffActivity
import com.example.shifty.databinding.ActivityPayrollBinding
import com.example.shifty.utils.startActivity
import com.google.firebase.auth.FirebaseAuth

class PayrollActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayrollBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPayrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
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
                startActivity<LoginActivity>(finish = true)
                finish()
            }

            cvPayStaff.setOnClickListener {
                startActivity<PayStaffActivity>(finish = false)
            }

            cvGenerateQR.setOnClickListener {
                startActivity<GenerateQRCodeActivity>(finish = false)
            }
        }
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