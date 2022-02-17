package com.ro.android.device_man

import android.R.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class CurrentDeviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_device)
/*
        val strValue01 = intent!!.getStringExtra("key").toString()
        // println("------------"+strValue01)
        val args = Bundle()
        args.putString("key",strValue01)
        // FragmentTransactionを生成。
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        // TestFragmentを生成。
        val fragment = CurrentListFragment()
        //Fragmentに渡す変数をセット
        fragment.arguments = args
        // FragmentTransactionに、TestFragmentをセット
        transaction.replace(R.id.frCurrentList, fragment)
        // FragmentTransactionをコミット
        transaction.commit()
*/
    }
}