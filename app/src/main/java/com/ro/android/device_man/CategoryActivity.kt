package com.ro.android.device_man

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class CategoryActivity : AppCompatActivity() {
    //private var myIntent: Intent? = null
    private var btEgd02: Button? = null
    private var btTcs02: Button? = null
    private var btErcpdbe02: Button? = null
    private var btOther02: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        findView()
    }

    private fun findView(){
        btEgd02 = findViewById<View>(R.id.btEgd02) as Button
        btTcs02 = findViewById<View>(R.id.btTcs02) as Button
        btErcpdbe02 = findViewById<View>(R.id.btErcpdbe02) as Button
        btOther02 = findViewById<View>(R.id.btOther02) as Button
    }

    fun onClickEgd(view: View){
        val myIntent = Intent(this@CategoryActivity,CurrentDeviceActivity::class.java)
        myIntent.putExtra("key","Egd")
        startActivity(myIntent)
    }

    fun onClickTcs(view: View){
        val myIntent = Intent(this@CategoryActivity,CurrentDeviceActivity::class.java)
        myIntent.putExtra("key","Tcs")
        startActivity(myIntent)
    }

    fun onClickErcpdbe(view: View){
        val myIntent = Intent(this@CategoryActivity,CurrentDeviceActivity::class.java)
        myIntent.putExtra("key","Ercpdbe")
        startActivity(myIntent)
    }

    fun onClickOther(view: View){
        val myIntent = Intent(this@CategoryActivity,CurrentDeviceActivity::class.java)
        myIntent.putExtra("key","Other")
        startActivity(myIntent)
    }

}