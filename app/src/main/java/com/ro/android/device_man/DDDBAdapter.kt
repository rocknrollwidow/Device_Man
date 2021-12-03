package com.ro.android.device_man

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

//廃棄済み処置具のDBA
class DDDBAdapter (private var context: Context ) {
    private var db: SQLiteDatabase? = null // SQLiteDatabase
    private var dbHelper: DBHelper? = null // DBHepler

    /**
     * DBの読み書き
     * openDB()
     *
     * @return this 自身のオブジェクト
     */
    fun openDB(): DDDBAdapter {
        db = dbHelper!!.writableDatabase // DBの読み書き
        return this
    }

    /**
     * DBの読み込み 今回は未使用
     * readDB()
     *
     * @return this 自身のオブジェクト
     */
    fun readDB(): DDDBAdapter {
        db = dbHelper!!.readableDatabase // DBの読み込み
        return this
    }

    /**
     * DBを閉じる
     * closeDB()
     */
    fun closeDB() {
        db!!.close() // DBを閉じる
        db = null
    }

    /**
     * DBのレコードへ登録
     * saveDB()
     *
     * @param product 品名
     * @param maker  メーカー
     * @param quantity  個数
     * @param constant   定数
     * @param category  種類
     */
    fun saveDB(id: String,name: String,type: String,number: Int,date_opened: String,date_disposed: String,
               reason: String,review: String,unusable: String,staff: String,pics: ByteArray){
        db!!.beginTransaction() // トランザクション開始
        try {
            val values = ContentValues() // ContentValuesでデータを設定していく
            values.put(COL_NAME,name)
            values.put(COL_TYPE,type)
            values.put(COL_NUMBER,number)
            values.put(COL_DATE_OPENED,date_opened)
            values.put(COL_DATE_DISPOSED,date_disposed)
            values.put(COL_REASON,reason)
            values.put(COL_REVIEW,review)
            values.put(COL_UNUSABLE,unusable)
            values.put(COL_STAFF,staff)
            values.put(COL_PICS,pics)

            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues
            db!!.insert(DB_TABLE, null, values) // レコードへ登録
            db!!.setTransactionSuccessful() // トランザクションへコミット
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db!!.endTransaction() // トランザクションの終了
        }
    }

    /**
     * DBのデータを取得
     * getDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    fun getDB(columns: Array<String?>?): Cursor {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db!!.query(DB_TABLE, columns, null, null, null, null, null)
    }


    /**
     * タップしたアイテムのデータを取得
     *
     * **/

    fun selectDB(columns: Array<String?>?,selection: String): Cursor {
        openDB()
        var selection = selection // 選択条件（WHERE句を指定）

        return db!!.query(DB_TABLE,columns,selection,null,null,null,null)
        closeDB()
    }

    fun updateDB(id: String,name: String,type: String,number: Int,date_opened: String,date_disposed: String,
                 reason: String,review: String,unusable: String,staff: String,pics: ByteArray){

        openDB()

        val values = ContentValues()
        values.put(COL_NAME,name)
        values.put(COL_TYPE,type)
        values.put(COL_NUMBER,number)
        values.put(COL_DATE_OPENED,date_opened)
        values.put(COL_DATE_DISPOSED,date_disposed)
        values.put(COL_REASON,reason)
        values.put(COL_REVIEW,review)
        values.put(COL_UNUSABLE,unusable)
        values.put(COL_STAFF,staff)
        values.put(COL_PICS,pics)
        db!!.update(DB_TABLE,values,"_id = ?",arrayOf(id))

        closeDB()
    }
/*
        fun changeQ(id: String,quantity:Int){
            openDB()
            val values = ContentValues()
            values.put(COL_QUANTITY,quantity)
            db!!.update(DB_TABLE,values,"_id = ?",arrayOf(id))
            closeDB()
        }

        fun changeC(id: String,constant: Int){
            openDB()
            val values = ContentValues()
            values.put(COL_CONSTANT,constant)
            db!!.update(DB_TABLE,values,"_id = ?",arrayOf(id))
            closeDB()
        }
*/

    /**
     * DBの検索したデータを取得
     * searchDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @param column  String 選択条件に使うカラム名
     * @param name    String[]
     * @return DBの検索したデータ
     */
    /*
    fun searchDB(columns: Array<String?>?): Cursor {
        return db!!.query(
            DB_TABLE, columns, "quantity < constant", null, null, null, null
        )
    }
    */

    /**
     * DBのレコードを全削除
     * allDelete()
     */
    fun allDelete() {
        db!!.beginTransaction() // トランザクション開始
        try {
            // deleteメソッド DBのレコードを削除
            // 第1引数：テーブル名
            // 第2引数：削除する条件式 nullの場合は全レコードを削除
            // 第3引数：第2引数で?を使用した場合に使用
            db!!.delete(DB_TABLE, null, null) // DBのレコードを全削除
            db!!.setTransactionSuccessful() // トランザクションへコミット
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db!!.endTransaction() // トランザクションの終了
        }
    }

    /**
     * DBのレコードの単一削除
     * selectDelete()
     *
     * @param position String
     */
    fun selectDelete(position: String) {
        openDB()
        db!!.delete(DB_TABLE, COL_ID + "=?", arrayOf(position))
        closeDB()
    }

    /**
     * データベースの生成やアップグレードを管理するSQLiteOpenHelperを継承したクラス
     * DBHelper
     */
    private class DBHelper  // コンストラクタ
        (context: Context?) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
        /**
         * DB生成時に呼ばれる
         * onCreate()
         *
         * @param db SQLiteDatabase
         */
        override fun onCreate(db: SQLiteDatabase) {

            //テーブルを作成するSQL文の定義 ※スペースに気を付ける
            val createTbl = ("CREATE TABLE " + DB_TABLE + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_NAME + " TEXT NOT NULL,"
                    + COL_TYPE + " TEXT NOT NULL,"
                    + COL_NUMBER + " INTEGER NOT NULL,"
                    + COL_DATE_OPENED + " TEXT NOT NULL,"
                    + COL_DATE_DISPOSED + " TEXT NOT NULL,"
                    + COL_REASON + " TEXT NOT NULL,"
                    + COL_REVIEW + " TEXT NOT NULL,"
                    + COL_UNUSABLE + " TEXT NOT NULL,"
                    + COL_STAFF + " TEXT NOT NULL,"
                    + COL_PICS + " BLOB NOT NULL"
                    + ");")
            db.execSQL(createTbl) //SQL文の実行
        }

        /**
         * DBアップグレード(バージョンアップ)時に呼ばれる
         *
         * @param db         SQLiteDatabase
         * @param oldVersion int 古いバージョン
         * @param newVersion int 新しいバージョン
         */
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE)
            // テーブル生成
            onCreate(db)
        }
    }

    companion object {
        private const val DB_NAME = "device.db" // DB名
        const val DB_TABLE = "disposedSheet" // DBのテーブル名
        private const val DB_VERSION = 1 // DBのバージョン

        /**
         * DBのカラム名
         */
        const val COL_ID = "_id" // id
        const val COL_NAME = "name" // 名称
        const val COL_TYPE = "type"// 使用部位
        const val COL_NUMBER = "number" // 管理番号
        const val COL_DATE_OPENED = "date_opened" // 導入日
        const val COL_DATE_DISPOSED = "date_disposed" // 廃棄日
        const val COL_REASON = "reason" // 廃棄理由
        const val COL_REVIEW = "review" // 備考
        const val COL_UNUSABLE = "unusable" // 使用可否
        const val COL_STAFF = "staff" // 対応者
        const val COL_PICS = "pics" // 画像
    }

    // コンストラクタ
    init {
        dbHelper = DBHelper(context)
    }

}