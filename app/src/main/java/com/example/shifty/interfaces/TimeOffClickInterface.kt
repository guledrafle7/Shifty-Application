package com.example.shifty.interfaces

import com.example.shifty.model.TimeOff


interface TimeOffClickInterface {
    fun onTimeOffClick(timeOff: TimeOff, isApprove: String)
}