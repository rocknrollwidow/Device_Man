package com.ro.android.device_man

class MyDisposedItem (var ID: Int,var name: String,var type: String,var number: String,
                      var date_opened: String,var date_disposed: String,
                      var reason: String,var review: String,var unusable: String,
                      var staff: String,var pics: ByteArray) {

    fun getId(): Int {
        return this.ID
    }

    init {
        this.ID = ID
        this.name = name
        this.type = type
        this.number = number
        this.date_opened = date_opened
        this.date_disposed = date_disposed
        this.reason = reason
        this.review = review
        this.unusable = unusable
        this.staff = staff
        this.pics = pics
    }
}