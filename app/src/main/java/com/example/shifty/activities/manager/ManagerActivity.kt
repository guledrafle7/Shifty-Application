package com.example.shifty.activities.manager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.activities.authentication.LoginActivity
import com.example.shifty.activities.bookshifts.BookShiftActivity
import com.example.shifty.activities.request.RequestActivity
import com.example.shifty.databinding.ActivityManagerBinding
import com.example.shifty.model.QrCode
import com.example.shifty.utils.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class ManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerBinding.inflate(layoutInflater)
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

            cvRequest.setOnClickListener {
                startActivity<RequestActivity>(finish = false)
            }

            cvBookShifts.setOnClickListener {
                startActivity<BookShiftActivity>(finish = false)
            }
        }
    }

    private fun closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START, true);
        }
    }

    private fun openDrawer() {
        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}