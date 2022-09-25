package com.example.dolist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dolist.db.MessageRep
import com.example.dolist.db.ToDoRep
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var toDoRep: ToDoRep


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        toDoRep = ToDoRep(this)


        val message = Message("here is a text"," title")

        toDoRep.addMessage(message)

        val messagesFromDB = toDoRep.MessageList

        println(messagesFromDB)

        val list = toDoRep.ToDoList

        toDoAdapter = ToDoAdapter(list, toDoRep)

        rvDoItems.adapter = toDoAdapter
        rvDoItems.layoutManager = LinearLayoutManager(this)

        btnAddToDo.setOnClickListener {
            val todoTitle = etDoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = ToDo(todoTitle)
                toDoAdapter.addToDo(todo)
                etDoTitle.text.clear()
            }
        }

        btnDeleteDone.setOnClickListener {
            toDoAdapter.deleteDoneTodos()
        }
    }
}