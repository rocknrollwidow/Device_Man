package com.ro.android.device_man

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.TextureView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*

class NewDeviceActivity : AppCompatActivity() {

    //Todo 画像情報の保存を未実装
    private var etName04: EditText? = null
    private var spType04: Spinner? = null
    private var etReview04: EditText? = null
    private var spStatus04: Spinner? = null
    private var etNumber04: EditText? = null
    private var btAdd04: Button? = null
    private var btDatePicker04: Button? = null
    //private var btAddPics04: Button? = null
    private var statusArray: Array<String?>? = null
    private var typeArray: Array<String?>? = null
    private var status_spAdapter: ArrayAdapter<*>? = null
    private var type_spAdapter: ArrayAdapter<*>? = null
    private var iv1: ImageView? = null
    private var iv1_HAS_IMG: Boolean = false
    private var iv2: ImageView? = null
    private var iv2_HAS_IMG: Boolean = false
    private var iv3: ImageView? = null
    private var iv3_HAS_IMG: Boolean = false
    private var iv4: ImageView? = null
    private var iv4_HAS_IMG: Boolean = false
    private var m_uri: Uri? = null
    private var mark: Int = 0

    companion object{
        @SuppressLint("StaticFieldLeak")
        var etDateOpened04: EditText? = null
        private const val REQUEST_CHOOSER = 1000
        private var URI_1: Uri? = null
        private var URI_2: Uri? = null
        private var URI_3: Uri? = null
        private var URI_4: Uri? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_device)
        statusArray = resources.getStringArray(R.array.sp_status)
        typeArray = resources.getStringArray(R.array.sp_type)
        findViews()
        status_spAdapter =
            ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, statusArray!!)
        status_spAdapter!!.setDropDownViewResource((android.R.layout.simple_dropdown_item_1line))
        type_spAdapter =
            ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, typeArray!!)
        type_spAdapter!!.setDropDownViewResource((android.R.layout.simple_dropdown_item_1line))
        spStatus04!!.adapter = status_spAdapter
        spType04!!.adapter = type_spAdapter
        init()

        spStatus04!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val text = parent?.selectedItem as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spType04!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val text = parent?.selectedItem as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
/*
        btAddPics04!!.setOnClickListener{  v  ->
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)

            showGallery(this,0)
        }
*/
        // 登録ボタン押下時処理
        btAdd04!!.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            saveList()
            iv1?.setImageURI(null)
            iv2?.setImageURI(null)
            iv3?.setImageURI(null)
            iv4?.setImageURI(null)
        }
    }


private fun findViews(){
    etName04 = findViewById<View>(R.id.etname04) as EditText
    spType04 = findViewById<View>(R.id.sptype04) as Spinner
    etDateOpened04 = findViewById<View>(R.id.etdateopened04) as EditText
    etReview04 = findViewById<View>(R.id.etreview04) as EditText
    spStatus04 = findViewById<View>((R.id.spstatus04)) as Spinner
    etNumber04 = findViewById<View>(R.id.etnumber04) as EditText
    btAdd04 = findViewById<View>(R.id.btadd04) as Button
    btDatePicker04 = findViewById<View>(R.id.btdatepicker04) as Button
 //   btAddPics04 = findViewById<View>(R.id.btaddpics04) as Button
    iv1 = findViewById<View>(R.id.iv104) as ImageView
    iv2 = findViewById<View>(R.id.iv204) as ImageView
    iv3 = findViewById<View>(R.id.iv304) as ImageView
    iv4 = findViewById<View>(R.id.iv404) as ImageView
}

private fun init() {
    etName04!!.setText("")
    etDateOpened04!!.setText("")
    etReview04!!.setText("")
    etNumber04!!.setText("")
    etName04!!.requestFocus() // フォーカスを処置具名のEditTextに指定
}

    fun showDatePickerDialog(view: View){
        val newFragment = AddDeviceDatePicker()
        newFragment.show(supportFragmentManager,"datePicker")
    }

    private fun showGallery(context: Context,marker: Int) {
        mark = marker
        //カメラの起動Intentの用意
        val photoName = System.currentTimeMillis().toString() + ".jpg"
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, photoName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        m_uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, m_uri)

        // ギャラリー用のIntent作成
        val intentGallery: Intent
        if (Build.VERSION.SDK_INT < 19) {
            intentGallery = Intent(Intent.ACTION_GET_CONTENT)
            intentGallery.setType("image/*")
        } else {
            intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE)
            intentGallery.setType("image/jpeg")
        }
        val intent: Intent = Intent.createChooser(intentCamera, "画像の選択")
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intentGallery))
        startActivityForResult(intent, REQUEST_CHOOSER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSER) {
            if (resultCode != AppCompatActivity.RESULT_OK) {
                // キャンセル時
                return
            }
            val resultUri: Uri = (if (data != null) data.getData() else m_uri)
                ?: // 取得失敗
                return

            // ギャラリーへスキャンを促す
            MediaScannerConnection.scanFile(this, arrayOf<String>(resultUri.getPath().toString()), arrayOf("image/jpeg"), null)

             when(mark){
                 1 ->{iv1!!.setImageURI(resultUri)
                     URI_1 = resultUri
                     iv1_HAS_IMG = true }
                 2 ->{iv2!!.setImageURI(resultUri)
                     URI_2 = resultUri
                     iv2_HAS_IMG = true }
                 3 ->{iv3!!.setImageURI(resultUri)
                     URI_3 = resultUri
                     iv3_HAS_IMG = true }
                 4 ->{iv4!!.setImageURI(resultUri)
                     URI_4 = resultUri
                     iv4_HAS_IMG = true }
                 else ->{iv1!!.setImageURI(resultUri)
                     URI_1 = resultUri
                     iv1_HAS_IMG = true }
             }

        }
    }

private fun saveList() {

    // 各EditTextで入力されたテキストを取得
    val strName04 = etName04!!.text.toString()
    val strDateOpened04 = etDateOpened04!!.text.toString()
    val strNumber04 = etNumber04!!.text.toString()
    val strReview04 = etReview04!!.text.toString()
    val strType04 = spType04!!.selectedItem.toString()
    val strStatus04 = spStatus04!!.selectedItem.toString()
    val strUri104 = URI_1.toString()
    val strUri204 = URI_2.toString()
    val strUri304 = URI_3.toString()
    val strUri404 = URI_4.toString()

    // EditTextが空白の場合
    if (strName04 == "" || strDateOpened04 == "" || strNumber04 == "" ||
        strType04 == "" || strStatus04 == "" ) {

        Toast.makeText(this@NewDeviceActivity, "必要項目が入力されていません。", Toast.LENGTH_SHORT).show()
    } else {        // EditTextが全て入力されている場合

        // DBへの登録処理
        val dbAdapter = CDDBAdapter(this)
        dbAdapter.openDB() // DBの読み書き
        dbAdapter.saveDB(strName04, strType04, strNumber04, strDateOpened04, strStatus04, strReview04, strUri104, strUri204, strUri304, strUri404) // DBに登録
        dbAdapter.closeDB() // DBを閉じる
        Toast.makeText(this@NewDeviceActivity, "処置具を追加しました。", Toast.LENGTH_SHORT).show()
        init() // 初期値設定
    }
}

       fun onItemClick_iv1(view: View) {

                val popupMenu = PopupMenu(this@NewDeviceActivity,view)
                popupMenu.menu.add(Menu.NONE, 0, 0, "拡大表示")
                popupMenu.menu.add(Menu.NONE, 1, 1, "追加・変更")
                popupMenu.menu.add(Menu.NONE,2,2,"削除")

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    val id = menuItem.itemId
                    if (id == 0) {
                        val myIntent = Intent(this@NewDeviceActivity,ShowImageActivity::class.java)
                        myIntent.putExtra("uri",URI_1)
                        startActivity(myIntent)
                    } else if(id == 1){
                        showGallery(this@NewDeviceActivity,1)
                    }else{
                        iv1!!.setImageURI(null)
                        iv1_HAS_IMG = false
                    }
                    false
                }
                popupMenu.show()
            }

    fun onItemClick_iv2(view: View) {

        val popupMenu = PopupMenu(this@NewDeviceActivity,view)
        popupMenu.menu.add(Menu.NONE, 0, 0, "拡大表示")
        popupMenu.menu.add(Menu.NONE, 1, 1, "追加・変更")
        popupMenu.menu.add(Menu.NONE,2,2,"削除")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                val myIntent = Intent(this@NewDeviceActivity,ShowImageActivity::class.java)
                myIntent.putExtra("uri",URI_2)
                startActivity(myIntent)
            } else if(id == 1){
                showGallery(this@NewDeviceActivity,2)
            }else{
                iv2!!.setImageURI(null)
                iv2_HAS_IMG = false
            }
            false
        }
        popupMenu.show()
    }

    fun onItemClick_iv3(view: View) {

        val popupMenu = PopupMenu(this@NewDeviceActivity,view)
        popupMenu.menu.add(Menu.NONE, 0, 0, "拡大表示")
        popupMenu.menu.add(Menu.NONE, 1, 1, "追加・変更")
        popupMenu.menu.add(Menu.NONE,2,2,"削除")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                val myIntent = Intent(this@NewDeviceActivity,ShowImageActivity::class.java)
                myIntent.putExtra("uri",URI_3)
                startActivity(myIntent)
            } else if(id == 1){
                showGallery(this@NewDeviceActivity,3)
            }else{
                iv3!!.setImageURI(null)
                iv3_HAS_IMG = false
            }
            false
        }
        popupMenu.show()
    }

    fun onItemClick_iv4(view: View) {

        val popupMenu = PopupMenu(this@NewDeviceActivity,view)
        popupMenu.menu.add(Menu.NONE, 0, 0, "拡大表示")
        popupMenu.menu.add(Menu.NONE, 1, 1, "追加・変更")
        popupMenu.menu.add(Menu.NONE,2,2,"削除")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                val myIntent = Intent(this@NewDeviceActivity,ShowImageActivity::class.java)
                myIntent.putExtra("uri",URI_4)
                startActivity(myIntent)
            } else if(id == 1){
                showGallery(this@NewDeviceActivity,4)
            }else{
                iv4!!.setImageURI(null)
                iv4_HAS_IMG = false
            }
            false
        }
        popupMenu.show()
    }

}
