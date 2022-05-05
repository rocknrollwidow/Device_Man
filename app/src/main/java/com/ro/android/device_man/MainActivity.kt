package com.ro.android.device_man

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var myIntent: Intent? = null
    private var btCurrent01: Button? = null
    private var btDisposed01: Button? = null
    private var btAdd01: Button? = null
    private var tvTitle01: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findView()
    }

    private fun findView(){
        btCurrent01 = findViewById<View>(R.id.btCurrent01) as Button
        btDisposed01 = findViewById<View>(R.id.btDisposed01) as Button
        btAdd01 = findViewById<View>(R.id.btAdd01) as Button
        tvTitle01 = findViewById<View>(R.id.tvTitle01) as TextView
    }

    fun onClickCurrent(view: View){
        myIntent = Intent(this@MainActivity,CurrentDeviceActivity::class.java)
        println("-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-MainActivity---onClickCurrent")
        startActivity(myIntent)
    }

    fun onClickDisposed(view: View){
        myIntent = Intent(this@MainActivity,DisposedDeviceActivity::class.java)
        startActivity(myIntent)
    }

    fun onClickAdd(view: View){
        myIntent = Intent(this@MainActivity,NewDeviceActivity::class.java)
        startActivity(myIntent)
    }

}