package com.ro.android.device_man

import android.R.*
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class CurrentDeviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        println("-;-;-;-;-;-;-;-;-;-;-;-;-;-;-CurrentDeviceActivity---onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_device)
    }
}