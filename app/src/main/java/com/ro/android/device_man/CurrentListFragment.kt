package com.ro.android.device_man

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class CurrentListFragment : Fragment() {
    private var cddbAdapter: CDDBAdapter? = null
    private var myBaseAdapter: MyBaseAdapter? = null
    private var items: MutableList<MyCurrentItem>? = null
    private var mlvCurrent: ListView? = null
    private var spType07: Spinner? = null
    private var typeArray: Array<String?>? = null
    private var type_spAdapter: ArrayAdapter<*>? = null
    private var myCurrentItem: MyCurrentItem? = null
    private var _isLayoutXLarge = true

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_current_list,container,false)
        mlvCurrent = view.findViewById(R.id.lvCurrent) as ListView
        items = ArrayList()
        cddbAdapter = CDDBAdapter(this.requireContext())
        myBaseAdapter = MyBaseAdapter(this.requireContext(), items as ArrayList<MyCurrentItem>)
        loadMyList()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spType07 = view.findViewById(R.id.sptype07) as Spinner
        typeArray = resources.getStringArray(R.array.sp_type)
        type_spAdapter =
            ArrayAdapter<String?>(this.requireContext(), R.layout.support_simple_spinner_dropdown_item, typeArray!!)
        type_spAdapter!!.setDropDownViewResource((android.R.layout.simple_dropdown_item_1line))
        spType07!!.adapter = type_spAdapter
        spType07!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val text = parent?.selectedItem as String
               if(text==""){
                   loadMyList()
               }else {
                   selectMyList(text)
               }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        mlvCurrent?.onItemClickListener = ListItemClickListener()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // 親クラスのメソッド呼び出し。
        super.onActivityCreated(savedInstanceState)
        // 自分が所属するアクティビティからmenuThanksFrameを取得。
        val currentDeviceFrame = activity?.findViewById<View>(R.id.currentDeviceFrame)
        // menuThanksFrameがnull、つまり存在しないなら…
        if(currentDeviceFrame == null) {
            // 画面判定フラグを通常画面とする。
            _isLayoutXLarge = false
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this){
            isEnabled = false
            requireActivity().finish()
        }
    }

     fun loadMyList() {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items!!.clear()
        cddbAdapter!!.openDB() // DBの読み込み(読み書きの方)

        val columns: Array<String?>? = null
   //     val selection = "type = '${type}'"//WHERE句の作成

        // DBのデータを取得
        val c = cddbAdapter!!.getDB(columns)
        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myCurrentItem = MyCurrentItem(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getString(9),
                    c.getString(10)
                )

                items!!.add(myCurrentItem!!) // 取得した要素をitemsに追加
            } while (c.moveToNext())
        }
        c.close()
        cddbAdapter!!.closeDB() // DBを閉じる
        mlvCurrent!!.adapter = myBaseAdapter // ListViewにmyBaseAdapterをセット
        myBaseAdapter!!.notifyDataSetChanged() // Viewの更新
    }

     fun selectMyList(type: String) {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items!!.clear()
        cddbAdapter!!.openDB() // DBの読み込み(読み書きの方)

        val columns: Array<String?>? = null
        val selection = "type = '${type}'"//WHERE句の作成

        // DBのデータを取得
        val c = cddbAdapter!!.selectDB(columns,selection)
        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myCurrentItem = MyCurrentItem(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getString(9),
                    c.getString(10)
                )

                items!!.add(myCurrentItem!!) // 取得した要素をitemsに追加
            } while (c.moveToNext())
        }
        c.close()
        cddbAdapter!!.closeDB() // DBを閉じる
        mlvCurrent!!.adapter = myBaseAdapter // ListViewにmyBaseAdapterをセット
        myBaseAdapter!!.notifyDataSetChanged() // Viewの更新
    }

    inner class ListItemClickListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long){

            val item = parent.getItemAtPosition(position) as MyCurrentItem
            val listId = item.getId()
            val name = item.name
            val type = item.type
            val number = item.number
            val dateOpened = item.date_opened
            val status = item.status
            val review = item.review
            val uri1 = item.uri1
            val uri2 = item.uri2
            val uri3 = item.uri3
            val uri4 = item.uri4

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
            bundle.putString("uri1",uri1)
            bundle.putString("uri2",uri2)
            bundle.putString("uri3",uri3)
            bundle.putString("uri4",uri4)

            // フラグメントトランザクションの開始。
            val transaction = fragmentManager?.beginTransaction()
            // 注文完了フラグメントを生成。
            val currentDeviceInfoFragment = CurrentDeviceInfoFragment()
            // 引き継ぎデータを注文完了フラグメントに格納。
            currentDeviceInfoFragment.arguments = bundle
            // 生成した注文完了フラグメントをmenuThanksFrameレイアウト部品に追加(置き換え)。
            transaction?.replace(R.id.currentDeviceFrame, currentDeviceInfoFragment)
            // フラグメントトランザクションのコミット。
            transaction?.commit()

        }
    }

    inner class MyBaseAdapter     // コンストラクタの生成
        (private val context: Context, private val items: List<MyCurrentItem>):
        BaseAdapter() {
        // 毎回findViewByIdをする事なく、高速化が出来るようするholderクラス
        private inner class ViewHolder {
            var tv_current_name: TextView? = null
            var tv_current_number: TextView? = null
            var tv_current_num_sub: TextView? = null
        }

        // Listの要素数を返す
        override fun getCount(): Int {
            return items.size
        }

        // indexやオブジェクトを返す
        override fun getItem(position: Int): Any {
            return items[position]
        }

        // IDを他のindexに返す
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        // 新しいデータが表示されるタイミングで呼び出される
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var view = convertView
            val holder: ViewHolder

            // データを取得
            myCurrentItem = items[position]
            if (view == null) {
                val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(R.layout.row_sheet_current_list, parent, false)
                val tv_current_name =
                    view.findViewById<View>(R.id.tv_current_name07) as TextView // 品名のTextView
                val tv_current_number =
                    view.findViewById<View>(R.id.tv_current_number07) as TextView //規格のTextView
                val tv_current_num_sub = view.findViewById<View>(R.id.tv_current_num_sub07) as TextView

                // holderにviewを持たせておく
                holder = ViewHolder()
                holder.tv_current_name = tv_current_name
                holder.tv_current_number = tv_current_number
                holder.tv_current_num_sub = tv_current_num_sub
                view.tag = holder
            } else {
                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = view.tag as ViewHolder
            }

            // 取得した各データを各TextViewにセット
            holder.tv_current_name?.text = myCurrentItem!!.name
            holder.tv_current_number?.text = myCurrentItem!!.number
            if(myCurrentItem?.review != "") {
                holder.tv_current_name?.setTextColor(Color.YELLOW)
                holder.tv_current_number?.setTextColor(Color.YELLOW)
                holder.tv_current_num_sub?.setTextColor(Color.YELLOW)
            }

            return view
        }
    }
}