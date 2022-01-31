package com.ro.android.device_man

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
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
    private var ttvPic03_1: TextureView? = null
    private var ttvPic03_2: TextureView? = null
    private var ttvPic03_3: TextureView? = null
    private var ttvPic03_4: TextureView? = null
    private var btAddPics03: Button? = null
    private var btUpdate03: Button? = null
    private var btDispose03: Button? = null
    private var btDatePicker03: Button? = null
    private var myCurrentItem: MyCurrentItem? = null
    private var columns: Array<String?>? = null
    private var name: String? = null
    private var type: String? = null
    private var number: String? = null
    private var dateOpened: String? = null
    private var status: String? = null
    private var review: String? = null
    private var type_spAdapter: ArrayAdapter<*>? = null
    private var status_spAdapter: ArrayAdapter<*>? = null
    private var typeArray: Array<String?>? = null
    private var statusArray: Array<String?>? = null
    private var myCurrentListFragment: CurrentListFragment? = null

    companion object{
        @SuppressLint("StaticFieldLeak")
        var etDateOpened03: EditText? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentListFragment = fragmentManager?.findFragmentById(R.id.frCurrentList)
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
        spType03!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

        btDatePicker03!!.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)

            showDatePicker(view)
        }
        btUpdate03!!.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            // DBに登録
            onClickUpdateCurrent(view)
        }

        var indexType: Int = 0
        var indexStatus: Int = 0
        listId = arguments?.getString("listId")
        name = arguments?.getString("name")
        type = arguments?.getString("type")
        number = arguments?.getString("number")
        dateOpened = arguments?.getString("dateOpened")
        status = arguments?.getString("status")
        review = arguments?.getString("review")

        when(type){
            "EGD"->{indexType = 0}
            "TCS"->{indexType = 1}
            "ERCP・DBE"->{indexType = 2}
            else->{indexType = 3}
        }

        when(status){
            "使用可能"->{indexStatus = 0}
            else->{indexStatus = 1}
        }

        etName03!!.setText(name)
        spType03!!.setSelection(indexType)
        etNumber03!!.setText(number)
        etDateOpened03!!.setText(dateOpened)
        spStatus03!!.setSelection(indexStatus)
        etReview03!!.setText(review)

        return view
    }
/*
    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this){
            isEnabled = false
        }
    }
*/
   private fun onClickUpdateCurrent(view: View){

        val id = listId.toString()
        val name = etName03!!.text.toString()
        val type = spType03!!.selectedItem.toString()
        val dateopened = etDateOpened03!!.text.toString()
        val number = etNumber03!!.text.toString()
        val status = spStatus03!!.selectedItem.toString()
        val review = etReview03!!.text.toString()

        cddbAdapter!!.updateDB(id,name,type,number,dateopened,status,review)

        Toast.makeText(this.requireContext(), "情報を変更しました。", Toast.LENGTH_SHORT).show()
    }

   private fun showDatePicker(view: View){
        val newFragment = CurrentDeviceDatePicker()
        newFragment.show(childFragmentManager,"datePicker")
    }
}