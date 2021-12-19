package com.ro.android.device_man

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*

class NewDeviceActivity : AppCompatActivity() {

    //Todo 画像情報の保存を未実装

    private var etName04: EditText? = null
    private var spType04: Spinner? = null
    private var etDateOpened04: EditText? = null
    private var etReview04: EditText? = null
    private var spStatus04: Spinner? = null
    private var etNumber04: EditText? = null
    private var spStaff04: Spinner? = null
    private var btAdd04: Button? = null
    private var btAddPics04: Button? = null
    private var statusArray: Array<String?>? = null
    private var typeArray: Array<String?>? = null
    private var status_spAdapter: ArrayAdapter<*>? = null
    private var type_spAdapter: ArrayAdapter<*>? = null
    private var txv1: TextureView? = null
    private var txv2: TextureView? = null
    private var txv3: TextureView? = null
    private var txv4: TextureView? = null

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


        // 登録ボタン押下時処理
        btAdd04!!.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            saveList()
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
    btAddPics04 = findViewById<View>(R.id.btaddpics04) as Button

}

private fun init() {
    etName04!!.setText("")
    etDateOpened04!!.setText("")
    etReview04!!.setText("")
    etNumber04!!.setText("")
    etName04!!.requestFocus() // フォーカスを処置具名のEditTextに指定
}

private fun saveList() {

    // 各EditTextで入力されたテキストを取得
    val strName04 = etName04!!.text.toString()
    val strDateOpened04 = etDateOpened04!!.text.toString()
    val strNumber04 = etNumber04!!.text.toString()
    val strReview04 = etReview04!!.text.toString()
    val strType04 = spType04!!.selectedItem.toString()
    val strStatus04 = spStatus04!!.selectedItem.toString()


    // EditTextが空白の場合
    if (strName04 == "" || strDateOpened04 == "" || strNumber04 == "" || strReview04 == "" ||
        strType04 == "" || strStatus04 == "" ) {

        Toast.makeText(this@NewDeviceActivity, "必要項目が入力されていません。", Toast.LENGTH_SHORT).show()
    } else {        // EditTextが全て入力されている場合

        /*
        // 入力された定数と個数は文字列からint型へ変換
        val iQuantity = strQuantityA.toInt()
        val iConstant = strConstantA.toInt()
         */

        // DBへの登録処理
        val dbAdapter = CDDBAdapter(this)
        dbAdapter.openDB() // DBの読み書き
        dbAdapter.saveDB(strName04, strType04, strNumber04, strDateOpened04, strStatus04, strReview04) // DBに登録
        dbAdapter.closeDB() // DBを閉じる
        Toast.makeText(this@NewDeviceActivity, "物品を追加しました。", Toast.LENGTH_SHORT).show()
        init() // 初期値設定
    }
}


}