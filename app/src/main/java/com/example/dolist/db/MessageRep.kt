package com.example.dolist.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dolist.Message

class MessageRep(context : Context) : SQLiteOpenHelper(context, MessageRep.DATABASE_NAME, null, MessageRep.DATABASE_VER) {

    companion object {
        private val DATABASE_VER = 1
        private val DATABASE_NAME = "do_list.db"

        //Table
        private val TABLE_NAME = "message"
        private val COL_ID = "id"
        private val COL_TITLE = "title"
        private val COL_MESSAGE = "message"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY: String =
            ("CREATE TABLE  $TABLE_NAME ($COL_ID  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, $COL_TITLE  TEXT, $COL_MESSAGE TEXT)")

        p0!!.execSQL("CREATE TABLE  $TABLE_NAME ($COL_ID  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, $COL_TITLE  TEXT, $COL_MESSAGE TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    val MessageList: MutableList<Message>
        @SuppressLint("Range")
        get() {
            val messages = ArrayList<Message>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val message = Message(
                        cursor.getString(cursor.getColumnIndex(COL_MESSAGE)),
                        cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                        cursor.getInt(cursor.getColumnIndex(COL_ID))
                    )
                    messages.add(message)
                } while (cursor.moveToNext())
            }
            db.close()
            return messages
        }

    fun addMessage(message: Message) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, message.id)
        values.put(COL_TITLE, message.title)
        values.put(COL_MESSAGE, message.text)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateMessage(message: Message): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, message.id)
        values.put(COL_TITLE, message.title)
        values.put(COL_MESSAGE, message.text)

        return db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(message.id.toString()))

    }

    fun deleteMessage(message: Message) {
        val db = this.writableDatabase


        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(message.id.toString()))
        db.close()
    }
}