package com.ro.android.device_man

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.system.Os.remove
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class DisposeFragment : DialogFragment() {

    private var btDatepicker06: Button? = null
    private var etReason06: EditText? = null
    private var etStaff06: EditText? = null
    private var btDispose06: Button? = null

    companion object{
        @SuppressLint("StaticFieldLeak")
        var etDateDisposed06: EditText? = null
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = Dialog(requireContext())
        val dw = dialog.window
        dw?.let{
            it.requestFeature(Window.FEATURE_NO_TITLE)
            it.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
            it.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
        dialog.setContentView(R.layout.dialog_dispose)
        etDateDisposed06 = dialog.findViewById<View>(R.id.etDateDisposed06) as EditText
        btDatepicker06 = dialog.findViewById<View>(R.id.btDatepicker06) as Button
        etReason06 = dialog.findViewById<View>(R.id.etReason06) as EditText
        etStaff06 = dialog.findViewById<View>(R.id.etStaff06) as EditText
        btDispose06 = dialog.findViewById<View>(R.id.btDispose06) as Button

        btDatepicker06!!.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)

            showDatePicker()
        }

        btDispose06!!.setOnClickListener { v -> // キーボードを非表示
            val inputMethodManager =
                requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            onClickDispose()
        }
        return dialog
    }

    private fun showDatePicker(){
        val newFragment = DisposeDialogDatePicker()
        newFragment.show(childFragmentManager,"datePicker")
    }

    private fun onClickDispose(){
        val listId = arguments?.getString("listId").toString()
        val name = arguments?.getString("name").toString()
        val type = arguments?.getString("type").toString()
        val number = arguments?.getString("number").toString()
        val dateOpened = arguments?.getString("dateOpened").toString()
        val dateDisposed = etDateDisposed06!!.text.toString()
        val reason = etReason06!!.text.toString()
        val review = arguments?.getString("review").toString()
        val status = arguments?.getString("status").toString()
        val staff = etStaff06!!.text.toString()
        val uri1 = arguments?.getString("uri1").toString()
        val uri2 = arguments?.getString("uri2").toString()
        val uri3 = arguments?.getString("uri3").toString()
        val uri4 = arguments?.getString("uri4").toString()

        val dddbAdapter = DDDBAdapter(this.requireContext())
        dddbAdapter.openDB()
        dddbAdapter.saveDB(listId,name,type,number,dateOpened,dateDisposed,reason,review,status,staff,uri1,uri2,uri3,uri4)
        dddbAdapter.closeDB()
        val cdbAdapter = CDDBAdapter(this.requireContext())
        cdbAdapter.openDB() // DBの読み書き
        cdbAdapter.selectDelete(listId) // DBに登録
        cdbAdapter.closeDB() // DBを閉じる

        Toast.makeText(this.requireContext(), "情報を変更しました。", Toast.LENGTH_SHORT).show()
        this.dismiss()
    }


}