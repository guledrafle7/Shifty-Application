package com.example.shifty.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shifty.databinding.CustomTimeOffRequestLayoutBinding
import com.example.shifty.interfaces.TimeOffClickInterface
import com.example.shifty.model.TimeOff
import com.example.shifty.utils.showLog

class TimeOffAdapter(
    private val clickInterface: TimeOffClickInterface
) : RecyclerView.Adapter<TimeOffAdapter.ViewHolder>() {

    private var list = ArrayList<TimeOff>()

    inner class ViewHolder(val binding: CustomTimeOffRequestLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomTimeOffRequestLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(list[position]) {
                binding.tvEmpId.text = "Emp ID: ${this.uid}"
                binding.tvOffDates.text = "Date selected: ${this.startDate} - ${this.endDate}"

                when (this.isAllowed.trim()) {
                    "1" -> {
                        binding.tvApprove.text = "Approved"
                        binding.tvDeny.visibility = View.GONE
                    }
                    "-1" -> {
                        binding.tvDeny.text = "Denied"
                        binding.tvApprove.visibility = View.GONE
                    }
                    else -> {
                        binding.tvApprove.setOnClickListener {
                            clickInterface.onTimeOffClick(timeOff = this, isApprove = "1")
                        }

                        binding.tvDeny.setOnClickListener {
                            clickInterface.onTimeOffClick(timeOff = this, isApprove = "-1")
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<TimeOff>) {
        if (list.isNotEmpty()) {
            list.clear()
        }
        list.addAll(newList)
        notifyDataSetChanged()
    }
}