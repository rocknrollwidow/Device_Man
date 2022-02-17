package com.ro.android.device_man

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
    private var ttvPic05_1: TextureView? = null
    private var ttvPic05_2: TextureView? = null
    private var btAddPics05: Button? = null
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
    private var type_spAdapter: ArrayAdapter<*>? = null
    private var status_spAdapter: ArrayAdapter<*>? = null
    private var typeArray: Array<String?>? = null
    private var statusArray: Array<String?>? = null

    companion object{
        @SuppressLint("StaticFieldLeak")
        var etDateOpened05: EditText? = null
        var etDateDisposed05: EditText? = null
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
        btAddPics05 = view.findViewById<View>(R.id.btaddpics05) as Button
        btUpdate05 = view.findViewById<View>(R.id.btupdate05) as Button
        btDatePicker05 = view.findViewById<View>(R.id.btdatepicker05) as Button

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

        dddbAdapter!!.updateDB(id,name,type,number,dateopened,datedisposed,reason,review,status,staff)

        Toast.makeText(this.requireContext(), "情報を変更しました。", Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker(view: View){
        val newFragment = DisposeDeviceDatepicker()
        newFragment.show(childFragmentManager,"datePicker")
    }


}