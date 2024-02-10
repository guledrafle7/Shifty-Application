package com.example.shifty.activities.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.activities.employee.EmployeeActivity
import com.example.shifty.activities.manager.ManagerActivity
import com.example.shifty.activities.payroll.PayrollActivity
import com.example.shifty.databinding.ActivityLoginBinding
import com.example.shifty.model.Users
import com.example.shifty.utils.Constant
import com.example.shifty.utils.Constant.Companion.EMPLOYEE
import com.example.shifty.utils.Constant.Companion.MANAGER
import com.example.shifty.utils.Constant.Companion.PAYROLL
import com.example.shifty.utils.Constant.Companion.editor
import com.example.shifty.utils.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            if(isInputValid()) {
                val email=binding.inputEditTextEmail.text.toString()
                val password=binding.inputEditTextPassword.text.toString()

                showLoading()
                DataBaseOperations.authenticate(email,password,::authenticationResult)
            }
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

    }

    private fun showLoading() {
        binding.progressBar.visibility= View.VISIBLE
        binding.btnLogin.text=""
        binding.btnLogin.isEnabled=false
    }

    private fun hideLoading() {
        binding.progressBar.visibility= View.GONE
        binding.btnLogin.text= "Login"
        binding.btnLogin.isEnabled=true
    }

    private fun showMessage(message: String) {
        Toast.makeText(this@LoginActivity,message,Toast.LENGTH_SHORT).show()
    }

    private fun authenticationResult(error:Boolean,message:String,user:Users?) {
        hideLoading()
        showMessage(message)
        if(!error) {
            //It allows executing a block of code only if the user object is not null
            user?.let {
                when (it.role) {
                    EMPLOYEE -> startActivity<EmployeeActivity>(finish = true)
                    MANAGER -> startActivity<ManagerActivity>(finish = true)
                    PAYROLL -> startActivity<PayrollActivity>(finish = true)
                    else -> showMessage("Role not found...")
                }
            }
        }
    }

    private fun isInputValid(): Boolean
    {
        var  isValid=true
        binding.inputEmail.error=null
        binding.inputPassword.error=null

        if(binding.inputEditTextEmail.text.toString().isEmpty()) {
            binding.inputEmail.error="Enter email"
            binding.inputEmail.requestFocus()
            isValid=false
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEditTextEmail.text.toString()).matches()) {
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