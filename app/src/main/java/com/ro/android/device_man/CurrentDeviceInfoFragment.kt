package com.ro.android.device_man

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import java.time.LocalDate

class CurrentDeviceInfoFragment : Fragment() {

    private var _isLayoutLarge =  true
    private var intent: Intent? = null
    private var cddbAdapter: CDDBAdapter? = null
    private var spAdapter: ArrayAdapter<*>? = null
    private var listId: Int = -1
    private var items: MutableList<MyCurrentItem>? = null
    private var etName03: EditText? = null
    private var spType03: Spinner? = null
    //private var etDateOpened03: EditText? = null
    private var etNumber03: EditText? = null
    private var etStatus03: EditText? = null
    private var etReview03: EditText? = null
    private var ttvPic03_1: TextureView? = null
    private var ttvPic03_2: TextureView? = null
    private var ttvPic03_3: TextureView? = null
    private var ttvPic03_4: TextureView? = null
    private var btAddPics03: Button? = null
    private var btUpdate03: Button? = null
    private var btDispose03: Button? = null
    private var btDatePicker03: Button? = null
    private var columns: Array<String?>? = null


    companion object{
        var etDateOpened03: EditText? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentListFragment = fragmentManager?.findFragmentById(R.id.lvCurrent)
        if(currentListFragment == null){
            _isLayoutLarge = false
        }


        etDateOpened03!!.setText(LocalDate.now().toString())//>API26

        cddbAdapter = CDDBAdapter(this.requireContext())
        items = ArrayList()
        setFragmentResultListener("key"){requestKey, bundle ->
            val listId = bundle.getString("key1")
            val name = bundle.getString("key2")
            val type = bundle.getString("key3")
            val number = bundle.getString("key4")
            val dateOpened = bundle.getString("key5")
            val status = bundle.getString("key6")
            val review = bundle.getString("key7")
            val unusable = bundle.getString("key8")
            val pics1 = bundle.getByteArray("key9")
        }

        spType03!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val text = parent?.selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{

        val view = inflater.inflate(R.layout.fragment_current_device_info,container,false)
        val extras: Bundle?
        etName03 = view.findViewById<View>(R.id.etname03) as EditText
        spType03 = view.findViewById<View>(R.id.sptype03) as Spinner
        etDateOpened03 = view.findViewById<View>(R.id.etdateopened03) as EditText
        etNumber03 = view.findViewById<View>(R.id.etnumber03) as EditText
        etStatus03 = view.findViewById<View>(R.id.etstatus03) as EditText
        etReview03 = view.findViewById<View>(R.id.etreview03) as EditText
        btAddPics03 = view.findViewById<View>(R.id.btaddpics03) as Button
        btUpdate03 = view.findViewById<View>(R.id.btupdate03) as Button
        btDispose03 = view.findViewById<View>(R.id.btdispose03) as Button
        btDatePicker03 = view.findViewById<View>(R.id.btdatepicker03) as Button

        if(_isLayoutLarge){
            extras = arguments
        }else{
            val intent = activity?.intent
            extras = intent?.extras
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this){
            isEnabled = false
        }
    }



    fun onClickWarehouseButton(view: View){

        val id = listId.toString()
        val product = mtvProductW!!.text.toString()
        val maker = mtvMakerW!!.text.toString()
        val constant = mtvConstantW!!.text.toString()
        val quantity = mtvQuantityW!!.text.toString()
        val warehousing = metWarehousing!!.text.toString()
        val date = metDateW!!.text.toString()
        val staff = mspStaffW!!.selectedItem.toString()

        val iConstant = constant.toInt()
        val iQuantity = quantity.toInt()
        val iWarehousing = warehousing.toInt()

        whdbAdapter!!.saveDB(product,maker,iQuantity,iWarehousing,staff,date)
        Toast.makeText(this@Warehousing, "情報を変更しました。", Toast.LENGTH_SHORT).show()

        val newQuantity: Int = iQuantity + iWarehousing
        mtvQuantityW!!.setText(newQuantity.toString())
        if(newQuantity > iConstant){

            val builder = AlertDialog.Builder(this@Warehousing)
            builder.setTitle("入庫と出庫")
            builder.setMessage("${product}は、入庫後の在庫数が定数を上回ります。定数を更新しますか？")

            builder.setNegativeButton("No"){
                    dialog,which ->
            }
            builder.setPositiveButton("Yes"){
                    dialog,which ->
                dbAdapter!!.changeC(id,newQuantity)
                mtvConstantW!!.setText(newQuantity.toString())
            }

            val dialog = builder.create()
            dialog.show()
            false
        }
        dbAdapter!!.changeQ(id,newQuantity)
    }


    fun showDatePickerDialog(view: View){
        val newFragment = DatePickW()
        newFragment.show(supportFragmentManager,"datePicker")
    }


}