package com.ro.android.device_man

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class DisposedListFragment : Fragment(){
    private var dddbAdapter: DDDBAdapter? = null
    private var myBaseAdapter: MyBaseAdapter? = null
    private var items: MutableList<MyDisposedItem>? = null
    private var mlvDisposed: ListView? = null
    private var myDisposedItem: MyDisposedItem? = null
    private val columns: Array<String?>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:  Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_disposed_list,container,false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        items = ArrayList()
        dddbAdapter = DDDBAdapter(this.requireContext())
        myBaseAdapter = MyBaseAdapter(this.requireContext(),items as ArrayList<MyDisposedItem>)
        mlvDisposed = view.findViewById(R.id.lvDisposed)
        loadMyList()
    }


    private fun loadMyList() {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items!!.clear()
        dddbAdapter!!.openDB() // DBの読み込み(読み書きの方)

        // DBのデータを取得
        val c = dddbAdapter!!.getDB(columns)
        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myDisposedItem = MyDisposedItem(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getString(9),
                    c.getString(10),
                    c.getBlob(11)
                )
                Log.d("取得したCursor(ID):", c.getInt(0).toString())
                Log.d("取得したCursor(Name):", c.getString(1))
                Log.d("取得したCursor(Type):", c.getString(2))
                Log.d("取得したCursor(Number):",c.getString(3))

                items!!.add(myDisposedItem!!) // 取得した要素をitemsに追加
            } while (c.moveToNext())
        }
        c.close()
        dddbAdapter!!.closeDB() // DBを閉じる
        mlvDisposed!!.adapter = myBaseAdapter // ListViewにmyBaseAdapterをセット
        myBaseAdapter!!.notifyDataSetChanged() // Viewの更新
    }

    inner class MyBaseAdapter     // コンストラクタの生成
        (private val context: Context, private val items: List<MyDisposedItem>):
        BaseAdapter() {
        // 毎回findViewByIdをする事なく、高速化が出来るようするholderクラス
        private inner class ViewHolder {
            var tv_disposed_name: TextView? = null
            var tv_disposed_date: TextView? = null
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
            myDisposedItem = items[position]
            if (view == null) {
                val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(R.layout.row_sheet_disposed_list, parent, false)
                val tv_disposed_name =
                    view.findViewById<View>(R.id.tv_disposed_name08) as TextView // 品名のTextView
                val tv_disposed_date =
                    view.findViewById<View>(R.id.tv_disposed_date08) as TextView //廃棄日のTextView

                // holderにviewを持たせておく
                holder = ViewHolder()
                holder.tv_disposed_name = tv_disposed_name
                holder.tv_disposed_date = tv_disposed_date
                view.tag = holder
            } else {
                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = view.tag as ViewHolder
            }

            // 取得した各データを各TextViewにセット
            holder.tv_disposed_name!!.text = myDisposedItem!!.name
            holder.tv_disposed_date!!.text = myDisposedItem!!.date_disposed
            return view
        }
    }

}


