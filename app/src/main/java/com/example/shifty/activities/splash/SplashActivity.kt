package com.example.shifty.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.R
import com.example.shifty.activities.authentication.LoginActivity
import com.example.shifty.activities.employee.EmployeeActivity
import com.example.shifty.activities.manager.ManagerActivity
import com.example.shifty.activities.payroll.PayrollActivity
import com.example.shifty.model.Users
import com.example.shifty.utils.Constant.Companion.EMPLOYEE
import com.example.shifty.utils.Constant.Companion.MANAGER
import com.example.shifty.utils.Constant.Companion.PAYROLL
import com.example.shifty.utils.Constant.Companion.currentUser
import com.example.shifty.utils.showLog
import com.example.shifty.utils.startActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(currentUser == null) {
            finish()
            startActivity(Intent(this,LoginActivity::class.java))
        } else {
            DataBaseOperations.loadUserData(currentUser.uid,::onResult)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    private fun onResult(error: Boolean, message: String, user: Users?) {
        if(error) {
            showMessage(message)
        } else {
            user?.let {
                when (user.role) {
                    EMPLOYEE -> startActivity<EmployeeActivity>(finish = true)
                    MANAGER -> startActivity<ManagerActivity>(finish = true)
                    PAYROLL -> startActivity<PayrollActivity>(finish = true)
                    else -> showMessage("Role not found...")
                }
            }
        }

        showLog(user.toString())
    }
}