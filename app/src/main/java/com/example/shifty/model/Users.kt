package com.example.shifty.model

data class Users (var uid:String,val email:String,val role:Int)
{
    constructor():this("","",0)

    override fun toString(): String {
        return email
    }
}