package com.example.dolist.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dolist.Message
import com.example.dolist.ToDo

class ToDoRep(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {

    companion object {
        private val DATABASE_VER = 2
        private val DATABASE_NAME = "do_list.db"

        //Table1
        private val TABLE_NAME = "todo"
        private val COL_ID = "id"
        private val COL_TITLE = "title"
        private val COL_CHECKED = "checked"

        //Table2
        private val MESSAGE_TABLE_NAME = "message"
        private val MESSAGE_COL_ID = "id"
        private val MESSAGE_COL_TITLE = "title"
        private val COL_MESSAGE = "message"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY: String =
            ("CREATE TABLE  $TABLE_NAME ($COL_ID  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, $COL_TITLE  TEXT, $COL_CHECKED BOOLEAN)")

        p0!!.execSQL("CREATE TABLE  $TABLE_NAME ($COL_ID  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, $COL_TITLE  TEXT, $COL_CHECKED BOOLEAN)")
        p0!!.execSQL("CREATE TABLE  $MESSAGE_TABLE_NAME ($MESSAGE_COL_ID  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, $MESSAGE_COL_TITLE  TEXT, $COL_MESSAGE TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        p0!!.execSQL("DROP TABLE IF EXISTS $MESSAGE_TABLE_NAME")
        onCreate(p0)
    }

    val ToDoList: MutableList<ToDo>
        @SuppressLint("Range")
        get() {
            val todos = ArrayList<ToDo>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val todo = ToDo(
                        cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                        cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getInt(cursor.getColumnIndex(COL_CHECKED)) > 0
                    )
                    todos.add(todo)
                } while (cursor.moveToNext())
            }
            db.close()
            return todos
        }

    fun addToDo(todo: ToDo) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, todo.id)
        values.put(COL_TITLE, todo.title)
        values.put(COL_CHECKED, todo.isChecked)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateToDo(todo: ToDo): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, todo.id)
        values.put(COL_TITLE, todo.title)
        values.put(COL_CHECKED, todo.isChecked)

        return db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(todo.id.toString()))

    }

    fun deleteToDo(todo: ToDo) {
        val db = this.writableDatabase


        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(todo.id.toString()))
        db.close()
    }

    val MessageList: MutableList<Message>
        @SuppressLint("Range")
        get() {
            val messages = ArrayList<Message>()
            val selectQuery = "SELECT * FROM ${ToDoRep.MESSAGE_TABLE_NAME}"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val message = Message(
                        cursor.getString(cursor.getColumnIndex(ToDoRep.COL_MESSAGE)),
                        cursor.getString(cursor.getColumnIndex(ToDoRep.MESSAGE_COL_TITLE)),
                        cursor.getInt(cursor.getColumnIndex(ToDoRep.MESSAGE_COL_ID))
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
        values.put(ToDoRep.MESSAGE_COL_ID, message.id)
        values.put(ToDoRep.MESSAGE_COL_TITLE, message.title)
        values.put(ToDoRep.COL_MESSAGE, message.text)

        db.insert(ToDoRep.MESSAGE_TABLE_NAME, null, values)
        db.close()
    }

    fun updateMessage(message: Message): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ToDoRep.MESSAGE_COL_ID, message.id)
        values.put(ToDoRep.MESSAGE_COL_TITLE, message.title)
        values.put(ToDoRep.COL_MESSAGE, message.text)

        return db.update(ToDoRep.MESSAGE_TABLE_NAME, values, "${ToDoRep.MESSAGE_COL_ID}=?", arrayOf(message.id.toString()))

    }

    fun deleteMessage(message: Message) {
        val db = this.writableDatabase


        db.delete(ToDoRep.MESSAGE_TABLE_NAME, "${ToDoRep.MESSAGE_COL_ID}=?", arrayOf(message.id.toString()))
        db.close()
    }
}