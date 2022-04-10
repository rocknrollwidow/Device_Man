package com.ro.android.device_man

class MyDisposedItem (var ID: Int,var name: String,var type: String,var number: String,
                      var date_opened: String,var date_disposed: String,
                      var reason: String,var review: String,var status: String,
                      var staff: String,var uri1: String,var uri2: String,var uri3: String,var uri4: String) {

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
        this.status = status
        this.staff = staff
        this.uri1 = uri1
        this.uri2 = uri2
        this.uri3 = uri3
        this.uri4 = uri4

    }
}