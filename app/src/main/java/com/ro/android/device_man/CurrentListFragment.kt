package com.ro.android.device_man

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult

//Todo 画像の取り扱いが未実装

class CurrentListFragment : Fragment(){
    private var cddbAdapter: CDDBAdapter? = null
    private var myBaseAdapter: MyBaseAdapter? = null
    private var items: MutableList<MyCurrentItem>? = null
    private var mlvCurrent: ListView? = null
    private var myCurrentItem: MyCurrentItem? = null
    private val columns: Array<String?>? = null

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState:  Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_current_list,container,false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        items = ArrayList()
        cddbAdapter = CDDBAdapter(this.requireContext())
        myBaseAdapter = MyBaseAdapter(this.requireContext(),items as ArrayList<MyCurrentItem>)
        mlvCurrent = view.findViewById(R.id.lvCurrent)
        loadMyList()
    }


    private fun loadMyList() {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items!!.clear()
        cddbAdapter!!.openDB() // DBの読み込み(読み書きの方)

        // DBのデータを取得
        val c = cddbAdapter!!.getDB(columns)
        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myCurrentItem = MyCurrentItem(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getString(9),
                    c.getBlob(10)
                )
                Log.d("取得したCursor(ID):", c.getInt(0).toString())
                Log.d("取得したCursor(Name):", c.getString(1))
                Log.d("取得したCursor(Type):", c.getString(2))
                Log.d("取得したCursor(Number):",c.getString(3))

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
            val unusable = item.unusable
            val pics1 = item.pics

            setFragmentResult("key", bundleOf(
                "key1" to listId,
                "key2" to name,
                "key3" to type,
                "key4" to number,
                "key5" to dateOpened,
                "key6" to status,
                "key7" to review,
                "key8" to unusable,
                "key9" to pics1
            ))


        }
    }

    inner class MyBaseAdapter     // コンストラクタの生成
        (private val context: Context, private val items: List<MyCurrentItem>):
        BaseAdapter() {
        // 毎回findViewByIdをする事なく、高速化が出来るようするholderクラス
        private inner class ViewHolder {
            var tv_current_name: TextView? = null
            var tv_current_number: TextView? = null
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

                // holderにviewを持たせておく
                holder = ViewHolder()
                holder.tv_current_name = tv_current_name
                holder.tv_current_number = tv_current_number
                view.tag = holder
            } else {
                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = view.tag as ViewHolder
            }

            // 取得した各データを各TextViewにセット
            holder.tv_current_name!!.text = myCurrentItem!!.name
            holder.tv_current_number!!.text = myCurrentItem!!.number
            return view
        }
    }

    }





