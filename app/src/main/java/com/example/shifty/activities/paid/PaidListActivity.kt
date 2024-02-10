package com.example.shifty.activities.paid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shifty.adapters.PaidAdapter
import com.example.shifty.adapters.TimeOffAdapter
import com.example.shifty.databinding.ActivityPaidListBinding
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.model.Pay
import com.example.shifty.utils.Constant.Companion.currentUser
import com.example.shifty.utils.isNetworkAvailable
import com.example.shifty.utils.showLog
import com.google.firebase.firestore.FirebaseFirestore

class PaidListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaidListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaidListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isNetworkAvailable())
            DataBaseOperations.getAllPaid(uid = currentUser!!.uid,::getPaid)

        binding.appbar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getPaid(pays: ArrayList<Pay>?) {
        if(pays != null){
            val adapter = PaidAdapter()

            binding.apply {
                rvList.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }

                rvList.adapter = adapter
                adapter.setList(pays)
                if(adapter.itemCount>0){
                    tvList.visibility = View.GONE
                    rvList.visibility = View.VISIBLE
                }
            }
        } else {
            binding.tvList.text = "No Record found"
        }
    }
}