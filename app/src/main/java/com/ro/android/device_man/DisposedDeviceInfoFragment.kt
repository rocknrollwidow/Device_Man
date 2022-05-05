package com.ro.android.device_man

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment

class DisposedDeviceInfoFragment : Fragment() {
    private var _isLayoutLarge =  true
    private var dddbAdapter: DDDBAdapter? = null
    private var listId: String? = null
    private var items: MutableList<MyCurrentItem>? = null
    private var etName05: EditText? = null
    private var spType05: Spinner? = null
    private var etNumber05: EditText? = null
    private var etReview05: EditText? = null
    private var etReason05: EditText? = null
    private var spStatus05: Spinner? = null
    private var etStaff05: EditText? = null
    private var iv1: ImageView? = null
    private var iv1_HAS_IMG: Boolean = false
    private var iv2: ImageView? = null
    private var iv2_HAS_IMG: Boolean = false
    private var iv3: ImageView? = null
    private var iv3_HAS_IMG: Boolean = false
    private var iv4: ImageView? = null
    private var iv4_HAS_IMG: Boolean = false
    private var btUpdate05: Button? = null
    private var btDatePicker05: Button? = null
    private var name: String? = null
    private var type: String? = null
    private var number: String? = null
    private var dateOpened: String? = null
    private var datedisposed: String? = null
    private var status: String? = null
    private var review: String? = null
    private var reason: String? = null
    private var staff: String? = null
    private var uri1: Uri? = null
    private var uri2: Uri? = null
    private var uri3: Uri? = null
    private var uri4: Uri? = null
    private var type_spAdapter: ArrayAdapter<*>? = null
    private var status_spAdapter: ArrayAdapter<*>? = null
    private var typeArray: Array<String?>? = null
    private var statusArray: Array<String?>? = null
    private var mark: Int = 0
    private var m_uri: Uri? = null

    companion object{
        @SuppressLint("StaticFieldLeak")
        var etDateOpened05: EditText? = null
        var etDateDisposed05: EditText? = null
        private const val REQUEST_CHOOSER = 1000
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val disposedListFragment = fragmentManager?.findFragmentById(R.id.frDisposedList)
        if(disposedListFragment == null){
            _isLayoutLarge = false
        }
        dddbAdapter = DDDBAdapter(this.requireContext())
        items = ArrayList()
    }

    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{

        val view = inflater.inflate(R.layout.fragment_disposed_deviceinfo,container,false)
        val extras: Bundle?
        if(_isLayoutLarge){
            extras = arguments
        }else{
            val intent = activity?.intent
            extras = intent?.extras
        }
        etName05 = view.findViewById<View>(R.id.etname05) as EditText
        spType05 = view.findViewById<View>(R.id.sptype05) as Spinner
        etDateOpened05 = view.findViewById<View>(R.id.etdateopened05) as EditText
        etDateDisposed05 = view.findViewById<View>(R.id.etdatedisposed05) as EditText
        etNumber05 = view.findViewById<View>(R.id.etnumber05) as EditText
        etReview05 = view.findViewById<View>(R.id.etreview05) as EditText
        etReason05 = view.findViewById<View>(R.id.etreason05) as EditText
        spStatus05 = view.findViewById<View>(R.id.spstatus05) as Spinner
        etStaff05 = view.findViewById<View>(R.id.etstaff05) as EditText
        btUpdate05 = view.findViewById<View>(R.id.btupdate05) as Button
        btDatePicker05 = view.findViewById<View>(R.id.btdatepicker05) as Button
        iv1 = view.findViewById<View>(R.id.iv105) as ImageView
        iv2 = view.findViewById<View>(R.id.iv205) as ImageView
        iv3 = view.findViewById<View>(R.id.iv305) as ImageView
        iv4 = view.findViewById<View>(R.id.iv405) as ImageView

        //Spinnerのinit
        statusArray = resources.getStringArray(R.array.sp_status)
        typeArray = resources.getStringArray(R.array.sp_type)
        status_spAdapter =
            ArrayAdapter<String>(this.requireContext(), R.layout.support_simple_spinner_dropdown_item, statusArray!!)
        status_spAdapter!!.setDropDownViewResource((android.R.layout.simple_dropdown_item_1line))
        type_spAdapter =
            ArrayAdapter<String?>(this.requireContext(), R.layout.support_simple_spinner_dropdown_item, typeArray!!)
        type_spAdapter!!.setDropDownViewResource((android.R.layout.simple_dropdown_item_1line))
        spStatus05!!.adapter = status_spAdapter
        spStatus05!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val text = parent?.selectedItem as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
        spType05!!.adapter = type_spAdapter
        spType05!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val text = parent?.selectedItem as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        btDatePicker05!!.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)

            showDatePicker(view)
        }
        btUpdate05!!.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onClickUpdateDisposed(view)
        }

        iv1?.setOnClickListener{ v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onItemClick_iv1(view)
        }

        iv2?.setOnClickListener{ v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onItemClick_iv2(view)
        }

        iv3?.setOnClickListener{ v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onItemClick_iv3(view)
        }

        iv4?.setOnClickListener{ v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onItemClick_iv4(view)
        }

        var indexType: Int = 0
        var indexStatus: Int = 0
        listId = arguments?.getString("listId")
        name = arguments?.getString("name")
        type = arguments?.getString("type")
        number = arguments?.getString("number")
        dateOpened = arguments?.getString("dateOpened")
        datedisposed = arguments?.getString("dateDisposed")
        reason = arguments?.getString("reason")
        review = arguments?.getString("review")
        status = arguments?.getString("status")
        staff = arguments?.getString("staff")
        uri1 = arguments?.getString("uri1")?.toUri()
        if(uri1 != null){iv1_HAS_IMG =true }
        uri2 = arguments?.getString("uri2")?.toUri()
        if(uri2 != null){iv2_HAS_IMG =true }
        uri3 = arguments?.getString("uri3")?.toUri()
        if(uri3 != null){iv3_HAS_IMG =true }
        uri4 = arguments?.getString("uri4")?.toUri()
        if(uri4 != null){iv4_HAS_IMG =true }

        when(type){
            "EGD"->{indexType = 1}
            "TCS"->{indexType = 2}
            "ERCP・DBE"->{indexType = 3}
            "その他"->{indexType = 4}
            else->{indexType = 0}
        }
        when(status){
            "使用可能"->{indexStatus = 0}
            else->{indexStatus = 1}
        }

        etName05!!.setText(name)
        spType05!!.setSelection(indexType)
        spStatus05!!.setSelection(indexStatus)
        etNumber05!!.setText(number)
        etDateOpened05!!.setText(dateOpened)
        etDateDisposed05!!.setText(datedisposed)
        etReview05!!.setText(review)
        etReason05!!.setText(reason)
        etStaff05!!.setText(staff)
        if(iv1_HAS_IMG){iv1?.setImageURI(uri1)}
        if(iv2_HAS_IMG){iv2?.setImageURI(uri2)}
        if(iv3_HAS_IMG){iv3?.setImageURI(uri3)}
        if(iv4_HAS_IMG){iv4?.setImageURI(uri4)}

        return view
    }

        override fun onResume() {
            super.onResume()
            requireActivity().onBackPressedDispatcher.addCallback(this){
                isEnabled = false
                requireActivity().finish()
            }
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
            MediaScannerConnection.scanFile(
                this.requireContext(),
                arrayOf<String>(resultUri.getPath().toString()),
                arrayOf("image/jpeg"),
                null
            )

            when (mark) {
                1 -> {
                    iv1?.setImageURI(resultUri)
                    uri1 = resultUri
                    iv1_HAS_IMG = true
                }
                2 -> {
                    iv2?.setImageURI(resultUri)
                    uri2 = resultUri
                    iv2_HAS_IMG = true
                }
                3 -> {
                    iv3?.setImageURI(resultUri)
                    uri3 = resultUri
                    iv3_HAS_IMG = true
                }
                4 -> {
                    iv4?.setImageURI(resultUri)
                    uri4 = resultUri
                    iv4_HAS_IMG = true
                }
                else -> {
                    iv1?.setImageURI(resultUri)
                    uri1 = resultUri
                    iv1_HAS_IMG = true
                }
            }
        }
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
        startActivityForResult(intent,REQUEST_CHOOSER)
    }



    private fun onItemClick_iv1(view: View) {

        val popupMenu = PopupMenu(this.requireContext(),iv1)
        popupMenu.menu.add(Menu.NONE, 0, 0, "拡大表示")
        popupMenu.menu.add(Menu.NONE, 1, 1, "追加・変更")
        popupMenu.menu.add(Menu.NONE,2,2,"削除")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                val myIntent = Intent(this.requireContext(),ShowImageActivity::class.java)
                myIntent.putExtra("uri", uri1)
                startActivity(myIntent)
            } else if(id == 1){
                showGallery(this.requireContext(),1)
            }else{
                iv1?.setImageURI(null)
                iv1_HAS_IMG = false
                uri1 = null
            }
            false
        }

        popupMenu.show()
    }

    private fun onItemClick_iv2(view: View) {

        val popupMenu = PopupMenu(this.requireContext(),iv2)
        popupMenu.menu.add(Menu.NONE, 0, 0, "拡大表示")
        popupMenu.menu.add(Menu.NONE, 1, 1, "追加・変更")
        popupMenu.menu.add(Menu.NONE,2,2,"削除")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                val myIntent = Intent(this.requireContext(),ShowImageActivity::class.java)
                myIntent.putExtra("uri", uri2)
                startActivity(myIntent)
            } else if(id == 1){
                showGallery(this.requireContext(),2)
            }else{
                iv2?.setImageURI(null)
                iv2_HAS_IMG = false
                uri2 = null
            }
            false
        }
        popupMenu.show()
    }

    private fun onItemClick_iv3(view: View) {

        val popupMenu = PopupMenu(this.requireContext(),iv3)
        popupMenu.menu.add(Menu.NONE, 0, 0, "拡大表示")
        popupMenu.menu.add(Menu.NONE, 1, 1, "追加・変更")
        popupMenu.menu.add(Menu.NONE,2,2,"削除")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                val myIntent = Intent(this.requireContext(),ShowImageActivity::class.java)
                myIntent.putExtra("uri", uri3)
                startActivity(myIntent)
            } else if(id == 1){
                showGallery(this.requireContext(),3)
            }else{
                iv3?.setImageURI(null)
                iv3_HAS_IMG = false
                uri3 = null
            }
            false
        }
        popupMenu.show()
    }

    private fun onItemClick_iv4(view: View) {

        val popupMenu = PopupMenu(this.requireContext(),iv4)
        popupMenu.menu.add(Menu.NONE, 0, 0, "拡大表示")
        popupMenu.menu.add(Menu.NONE, 1, 1, "追加・変更")
        popupMenu.menu.add(Menu.NONE,2,2,"削除")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                val myIntent = Intent(this.requireActivity(),ShowImageActivity::class.java)
                myIntent.putExtra("uri", uri4)
                startActivity(myIntent)
            } else if(id == 1){
                showGallery(this.requireContext(),4)
            }else{
                iv4?.setImageURI(null)
                iv4_HAS_IMG = false
                uri4 = null
            }
            false
        }
        popupMenu.show()
    }

    private fun onClickUpdateDisposed(view: View){

        val id = listId.toString()
        val name = etName05!!.text.toString()
        val type = spType05!!.selectedItem.toString()
        val dateopened = etDateOpened05!!.text.toString()
        val datedisposed = etDateDisposed05!!.text.toString()
        val number = etNumber05!!.text.toString()
        val status = spStatus05!!.selectedItem.toString()
        val review = etReview05!!.text.toString()
        val reason = etReason05!!.text.toString()
        val staff = etStaff05!!.text.toString()
        val struri1 = uri1.toString()
        val struri2 = uri2.toString()
        val struri3 = uri3.toString()
        val struri4 = uri4.toString()

        dddbAdapter!!.updateDB(id,name,type,number,dateopened,datedisposed,reason,review,status,staff,struri1,struri2,struri3,struri4)

        Toast.makeText(this.requireContext(), "情報を変更しました。", Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker(view: View){
        val newFragment = DisposeDeviceDatepicker()
        newFragment.show(childFragmentManager,"datePicker")
    }


}