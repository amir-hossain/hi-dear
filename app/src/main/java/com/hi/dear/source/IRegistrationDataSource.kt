package com.hi.dear.source

interface IRegistrationDataSource {
    fun register(id: String,name:String,photo:String, password: String,conPass:String): Boolean
}