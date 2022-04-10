package com.ro.android.device_man

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.fragment.app.*
import java.time.LocalDate

class CurrentDeviceInfoFragment : Fragment() {

    private var _isLayoutLarge =  true
    private var cddbAdapter: CDDBAdapter? = null
    private var listId: String? = null
    private var items: MutableList<MyCurrentItem>? = null
    private var etName03: EditText? = null
    private var spType03: Spinner? = null
    private var etNumber03: EditText? = null
    private var spStatus03: Spinner? = null
    private var etReview03: EditText? = null
    private var iv1: ImageView? = null
    private var iv1_HAS_IMG: Boolean = false
    private var iv2: ImageView? = null
    private var iv2_HAS_IMG: Boolean = false
    private var iv3: ImageView? = null
    private var iv3_HAS_IMG: Boolean = false
    private var iv4: ImageView? = null
    private var iv4_HAS_IMG: Boolean = false
    private var m_uri: Uri? = null
    private var btAddPics03: Button? = null
    private var btUpdate03: Button? = null
    private var btDispose03: Button? = null
    private var btDatePicker03: Button? = null
    private var name: String? = null
    private var type: String? = null
    private var number: String? = null
    private var dateOpened: String? = null
    private var status: String? = null
    private var review: String? = null
    private var uri1: Uri? = null
    private var uri2: Uri? = null
    private var uri3: Uri? = null
    private var uri4: Uri? = null
    private var mark: Int = 0
    private var type_spAdapter: ArrayAdapter<*>? = null
    private var status_spAdapter: ArrayAdapter<*>? = null
    private var typeArray: Array<String?>? = null
    private var statusArray: Array<String?>? = null
   private var myIntent: Intent? = null

    companion object{
        @SuppressLint("StaticFieldLeak")
        var etDateOpened03: EditText? = null
        private const val REQUEST_CHOOSER = 1000
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentListFragment = requireFragmentManager().findFragmentById(R.id.frCurrentList)
        if(currentListFragment == null){
            _isLayoutLarge = false
        }
        cddbAdapter = CDDBAdapter(this.requireContext())
        items = ArrayList()
    }

    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{

        val view = inflater.inflate(R.layout.fragment_current_device_info,container,false)
        val extras: Bundle?
        if(_isLayoutLarge){
            extras = arguments
        }else{
            val intent = activity?.intent
            extras = intent?.extras
        }
        etName03 = view.findViewById<View>(R.id.etname03) as EditText
        spType03 = view.findViewById<View>(R.id.sptype03) as Spinner
        etDateOpened03 = view.findViewById<View>(R.id.etdateopened03) as EditText
        etNumber03 = view.findViewById<View>(R.id.etnumber03) as EditText
        spStatus03 = view.findViewById<View>(R.id.spstatus03) as Spinner
        etReview03 = view.findViewById<View>(R.id.etreview03) as EditText
        btAddPics03 = view.findViewById<View>(R.id.btaddpics03) as Button
        btUpdate03 = view.findViewById<View>(R.id.btupdate03) as Button
        btDispose03 = view.findViewById<View>(R.id.btdispose03) as Button
        btDatePicker03 = view.findViewById<View>(R.id.btdatepicker03) as Button
        iv1 = view.findViewById<View>(R.id.iv103) as ImageView
        iv2 = view.findViewById<View>(R.id.iv203) as ImageView
        iv3 = view.findViewById<View>(R.id.iv303) as ImageView
        iv4 = view.findViewById<View>(R.id.iv403) as ImageView

        var indexType: Int = 0
        var indexStatus: Int = 0
        listId = arguments?.getString("listId")
        name = arguments?.getString("name")
        type = arguments?.getString("type")
        number = arguments?.getString("number")
        dateOpened = arguments?.getString("dateOpened")
        status = arguments?.getString("status")
        review = arguments?.getString("review")
        uri1 = arguments?.getString("uri1")?.toUri()
        if(uri1 != null){iv1_HAS_IMG =true }
        uri2 = arguments?.getString("uri2")?.toUri()
        if(uri2 != null){iv2_HAS_IMG =true }
        println("- - - - - - - - - uri2 ="+ uri2 + "iv2_HAS_IMG = " + iv2_HAS_IMG)
        uri3 = arguments?.getString("uri3")?.toUri()
        if(uri3 != null){iv3_HAS_IMG =true }
        println("- - - - - - - - - uri3 ="+ uri3 + "iv3_HAS_IMG = " + iv3_HAS_IMG)
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

        //Spinnerのinit
        statusArray = resources.getStringArray(R.array.sp_status)
        typeArray = resources.getStringArray(R.array.sp_type)
        status_spAdapter =
            ArrayAdapter<String>(this.requireContext(), R.layout.support_simple_spinner_dropdown_item, statusArray!!)
        status_spAdapter!!.setDropDownViewResource((android.R.layout.simple_dropdown_item_1line))
        type_spAdapter =
            ArrayAdapter<String?>(this.requireContext(), R.layout.support_simple_spinner_dropdown_item, typeArray!!)
        type_spAdapter!!.setDropDownViewResource((android.R.layout.simple_dropdown_item_1line))
        spStatus03!!.adapter = status_spAdapter
        spStatus03!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val text = parent?.selectedItem as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
        spType03!!.adapter = type_spAdapter
        spType03!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val text = parent?.selectedItem as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        etName03?.setText(name)
        spType03?.setSelection(indexType)
        etNumber03?.setText(number)
        etDateOpened03?.setText(dateOpened)
        spStatus03?.setSelection(indexStatus)
        etReview03?.setText(review)
        if(iv1_HAS_IMG){iv1?.setImageURI(uri1)}
        if(iv2_HAS_IMG){iv2?.setImageURI(uri2)}
        if(iv3_HAS_IMG){iv3?.setImageURI(uri3)}
        if(iv4_HAS_IMG){iv4?.setImageURI(uri4)}

        btDatePicker03?.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)

            showDatePicker(view)
        }

        btAddPics03?.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            showGallery(this.requireContext(),0)
        }

        btUpdate03?.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onClickUpdateCurrent(view)
        }

        iv1?.setOnClickListener{ v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onItemClick_iv1(view)
        }

        iv2?.setOnClickListener{ v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onItemClick_iv2(view)
        }

        iv3?.setOnClickListener{ v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onItemClick_iv3(view)
        }

        iv4?.setOnClickListener{ v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onItemClick_iv4(view)
        }

        btDispose03!!.setOnClickListener{ v ->
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)

            val id = listId.toString()
            val name = etName03?.text.toString()
            val type = spType03?.selectedItem.toString()
            val dateopened = etDateOpened03?.text.toString()
            val number = etNumber03?.text.toString()
            val status = spStatus03?.selectedItem.toString()
            val review = etReview03?.text.toString()

            // 引き継ぎデータをまとめて格納できるBundleオブジェクト生成。
            val bundle = Bundle()
            // Bundleオブジェクトに引き継ぎデータを格納。
            bundle.putString("listId", listId.toString())
            bundle.putString("name",name)
            bundle.putString("type",type)
            bundle.putString("number",number)
            bundle.putString("dateOpened",dateOpened)
            bundle.putString("status",status)
            bundle.putString("review",review)
            bundle.putString("uri1",uri1.toString())
            bundle.putString("uri2",uri2.toString())
            bundle.putString("uri3",uri3.toString())
            bundle.putString("uri4",uri4.toString())

            val newFragment = DisposeFragment()
            newFragment.arguments = bundle
            newFragment.show(childFragmentManager,"DisposeFragment")
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this){
            isEnabled = false
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
            MediaScannerConnection.scanFile(this.requireContext(), arrayOf<String>(resultUri.getPath().toString()), arrayOf("image/jpeg"), null)

            println("- - - - - - - - - - - -"+resultUri)
            println("- - - - - - - - - - - - -"+ mark)
            //btAddPic押下時の条件分岐
            if(mark == 0) {
                if (!iv1_HAS_IMG) {
                    iv1?.setImageURI(resultUri)
                    uri1 = resultUri
                    iv1_HAS_IMG = true
                    println("- - - - - - - - - - 1")
                } else if(!iv2_HAS_IMG){
                    iv2?.setImageURI(resultUri)
                    uri2 = resultUri
                    iv2_HAS_IMG = true
                    println("- - - - - - - - - - 2")
                }else if(!iv3_HAS_IMG){
                    iv3?.setImageURI(resultUri)
                    uri3 = resultUri
                    iv3_HAS_IMG = true
                    println("- - - - - - - - - - 3")
                }else if(!iv4_HAS_IMG){
                    iv4?.setImageURI(resultUri)
                    uri4 = resultUri
                    iv4_HAS_IMG = true
                    println("- - - - - - - - - - 4")
                }
            }else{//各ImageView押下時の分岐
                when(mark){
                    1 ->{iv1?.setImageURI(resultUri)
                        uri1 = resultUri
                        iv1_HAS_IMG = true }
                    2 ->{iv2?.setImageURI(resultUri)
                        uri2 = resultUri
                        iv2_HAS_IMG = true }
                    3 ->{iv3?.setImageURI(resultUri)
                        uri3 = resultUri
                        iv3_HAS_IMG = true }
                    4 ->{iv4?.setImageURI(resultUri)
                        uri4 = resultUri
                        iv4_HAS_IMG = true }
                    else ->{iv1?.setImageURI(resultUri)
                        uri1 = resultUri
                        iv1_HAS_IMG = true }
                }
            }
        }
    }

    private fun onItemClick_iv1(view: View) {

        val popupMenu = PopupMenu(this.requireContext(),iv1)
        popupMenu.menu.add(Menu.NONE, 0, 0, "拡大表示")
        popupMenu.menu.add(Menu.NONE, 1, 1, "変更")
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
        popupMenu.menu.add(Menu.NONE, 1, 1, "変更")
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
        popupMenu.menu.add(Menu.NONE, 1, 1, "変更")
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
        popupMenu.menu.add(Menu.NONE, 1, 1, "変更")
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

   private fun onClickUpdateCurrent(view: View){

        val id = listId.toString()
        val name = etName03?.text.toString()
        val type = spType03?.selectedItem.toString()
        val dateopened = etDateOpened03?.text.toString()
        val number = etNumber03?.text.toString()
        val status = spStatus03?.selectedItem.toString()
        val review = etReview03?.text.toString()
        val struri1: String
        if(uri1 != null){
            struri1 = uri1.toString()
        }else{
            struri1 = ""
        }
        val struri2: String
        if(uri2 != null){
           struri2 = uri2.toString()
        }else{
           struri2 = ""
        }
        val struri3: String
        if(uri3 != null){
           struri3 = uri3.toString()
        }else{
           struri3 = ""
        }
        val struri4: String
        if(uri4 != null){
           struri4 = uri4.toString()
        }else{
           struri4 = ""
        }

       println("++++++++++++++++++++++"+uri1)
       println("++++++++++++++++++++++"+uri2)
       println("++++++++++++++++++++++"+uri3)
       println("++++++++++++++++++++++"+uri4)

        cddbAdapter?.updateDB(id,name,type,number,dateopened,status,review,struri1,struri2,struri3,struri4)

       val bundle = Bundle()
       // フラグメントトランザクションの開始。
       val transaction = fragmentManager?.beginTransaction()
       // 注文完了フラグメントを生成。
       val currentListFragment = CurrentListFragment()
       currentListFragment.arguments = bundle
       // 生成した注文完了フラグメントをmenuThanksFrameレイアウト部品に追加(置き換え)。
       transaction?.replace(R.id.frCurrentList,currentListFragment)
       println("----------------replace実行")
       transaction?.addToBackStack(null)
       // フラグメントトランザクションのコミット。
       transaction?.commit()

       Toast.makeText(this.requireContext(), "情報を変更しました。", Toast.LENGTH_SHORT).show()
    }

   private fun showDatePicker(view: View){
        val newFragment = CurrentDeviceDatePicker()
        newFragment.show(childFragmentManager,"datePicker")
    }

}