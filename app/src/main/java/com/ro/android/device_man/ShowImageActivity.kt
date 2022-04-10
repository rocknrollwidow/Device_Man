package com.ro.android.device_man

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import java.net.URI

class ShowImageActivity : AppCompatActivity() {
    private var iv: ImageView? = null
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)
        iv = findViewById<View>(R.id.showImage) as ImageView
        uri = intent.getParcelableExtra("uri")
        iv!!.setImageURI(uri)
    }
}