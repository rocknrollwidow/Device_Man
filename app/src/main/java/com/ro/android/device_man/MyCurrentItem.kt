package com.ro.android.device_man

class MyCurrentItem (var ID: Int,var name: String,var type: String,var number: String,
                     var date_opened: String,var status: String,var review: String) {

    fun getId(): Int {
        return this.ID
    }

    init {
        this.ID = ID
        this.name = name
        this.type = type
        this.number = number
        this.date_opened = date_opened
        this.status = status
        this.review = review
     //   this.pics = pics
    }
}