package com.hi.dear.data.model.common

class ProfileData(
    var country: String? = null,
    var city: String? = null,
    var age: String? = null,
    var about: String? = null,
    var picList: MutableList<String>? = null,
) : UserCore(){
    fun setPicList(pic:String){
        if(picList == null){
            picList= mutableListOf()
        }
        picList!!.add(pic)
    }
}