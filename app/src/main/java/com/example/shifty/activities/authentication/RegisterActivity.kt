package com.example.shifty.activities.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.R
import com.example.shifty.activities.employee.EmployeeActivity
import com.example.shifty.activities.manager.ManagerActivity
import com.example.shifty.activities.payroll.PayrollActivity
import com.example.shifty.databinding.ActivityRegisterBinding
import com.example.shifty.model.TimeOff
import com.example.shifty.model.Users
import com.example.shifty.utils.Constant
import com.example.shifty.utils.Constant.Companion.EMPLOYEE
import com.example.shifty.utils.Constant.Companion.MANAGER
import com.example.shifty.utils.Constant.Companion.PAYROLL
import com.example.shifty.utils.showLog
import com.example.shifty.utils.startActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            if(isInputValid()) {
                val email=binding.inputEditTextEmail.text.toString()
                val password=binding.inputEditTextPassword.text.toString()
                val role = getSelectedRole()
                user = Users("",email,role)
                showLoading()
                DataBaseOperations.createUser(user,password,::authenticationResult)
            }
        }
    }

    private fun getSelectedRole(): Int {
        if(binding.radioGroup.checkedRadioButtonId==R.id.radioEmployee)
            return EMPLOYEE

        if(binding.radioGroup.checkedRadioButtonId==R.id.radioManager)
            return MANAGER

        return -1
    }

    private fun showLoading() {
        binding.progressBar.visibility= View.VISIBLE
        binding.btnRegister.text=""
        binding.btnRegister.isEnabled=false
    }

    private fun hideLoading() {
        binding.progressBar.visibility= View.GONE
        binding.btnRegister.text= "Register"
        binding.btnRegister.isEnabled=true
    }

    private fun showMessage(message: String) {
        Toast.makeText(this@RegisterActivity,message, Toast.LENGTH_SHORT).show()
    }

    private fun authenticationResult(error:Boolean,message:String?) {
        hideLoading()
        if(error) {
            showMessage(message!!)
        } else {
            if (::user.isInitialized) {
                when (user.role) {
                    EMPLOYEE -> startActivity<EmployeeActivity>(finish = true)
                    MANAGER -> startActivity<ManagerActivity>(finish = true)
                    else -> showMessage("Role not found...")
                }
            }

            if(user.role == EMPLOYEE){
                val timeOff = TimeOff(uid = user.uid, email = user.email, startDate = "", endDate = "", isAllowed = "-1")
                DataBaseOperations.saveTimeOff(timeOff,::onResult)
            }
        }
    }

    private fun onResult(result: Boolean, message: String) {
        if(result)
            showLog(message)
        else
            showLog(message)
    }

    private fun isInputValid(): Boolean {
        var  isValid=true

        binding.inputEmail.error=null
        binding.inputPassword.error=null

        if(binding.radioGroup.checkedRadioButtonId==-1) {
            isValid=false
            showMessage("Please select type")
        }

        if(binding.inputEditTextEmail.text.toString().isEmpty()) {
            binding.inputEmail.error="Enter email"
            binding.inputEmail.requestFocus()
            isValid=false
        }


        if(! Patterns.EMAIL_ADDRESS.matcher(binding.inputEditTextEmail.text.toString()).matches()) {
            binding.inputEmail.error="Enter email"
            binding.inputEmail.requestFocus()
            isValid=false
        }


        if(binding.inputEditTextPassword.text.toString().isEmpty()) {
            binding.inputPassword.error="Enter password"
            binding.inputPassword.requestFocus()
            isValid=false
        }

        return isValid
    }
}