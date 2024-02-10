package com.example.shifty.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shifty.databinding.CustomPaidItemLayoutBinding
import com.example.shifty.model.Pay

class PaidAdapter : RecyclerView.Adapter<PaidAdapter.ViewHolder>(){

    private var list = ArrayList<Pay>()

    inner class ViewHolder(val binding: CustomPaidItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomPaidItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(list[position]) {
                binding.tvPaidDate.text = "Paid Date: ${this.date}"
                binding.tvPaidAmount.text = this.earn
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<Pay>) {
        if (list.isNotEmpty()) {
            list.clear()
        }
        list.addAll(newList)
        notifyDataSetChanged()
    }
}