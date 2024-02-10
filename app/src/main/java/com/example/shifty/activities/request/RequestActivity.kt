package com.example.shifty.activities.request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shifty.adapters.TimeOffAdapter
import com.example.shifty.databinding.ActivityRequestBinding
import com.example.shifty.firebase.DataBaseOperations
import com.example.shifty.interfaces.TimeOffClickInterface
import com.example.shifty.model.TimeOff
import com.example.shifty.utils.showToast

class RequestActivity : AppCompatActivity(), TimeOffClickInterface {

    private lateinit var binding: ActivityRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DataBaseOperations.getAllTimeOffRequest(::getAllTimeOut)

        binding.appbar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getAllTimeOut(timeOffs: ArrayList<TimeOff>?) {
        if(timeOffs != null) {
            val adapter = TimeOffAdapter(this)

            binding.apply {
                rvList.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }

                rvList.adapter = adapter
                adapter.setList(timeOffs)
                if(adapter.itemCount>0){
                    tvList.visibility = View.GONE
                    rvList.visibility = View.VISIBLE
                } else {
                    rvList.visibility = View.GONE
                    tvList.visibility = View.VISIBLE
                    tvList.text = "No Record found"
                }
            }
        } else {
            binding.tvList.text = "No Record found"
        }
    }

    override fun onTimeOffClick(timeOff: TimeOff, isApprove: String) {
        DataBaseOperations.updateTimeOff(timeOff = timeOff, action = isApprove,::getResult)
    }

    private fun getResult(res: Boolean) {
        if(res){
            showToast("Successfully Done")
            DataBaseOperations.getAllTimeOffRequest(::getAllTimeOut)
        } else {
            showToast("Failed")
        }
    }
}