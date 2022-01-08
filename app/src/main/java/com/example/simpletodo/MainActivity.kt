package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // remove long clicked item and notify the adapter the change
                listOfTasks.removeAt(position)
                adapter.notifyItemRemoved(position)

                saveData()
            }

        }

        loadData()

        // Look up recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Create adapter and pass data in
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // Attach recycler view to adapter
        recyclerView.adapter = adapter

        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField).text
        // Detect when add button being clicked and add task
        findViewById<Button>(R.id.button).setOnClickListener{
            // Take string in the input field
            val inputString = inputTextField.toString()

            // Add that string into the list of tasks if it is not empty
            if (inputString.isNotEmpty())
                listOfTasks.add(inputString)
            // Notify adapter the data has changed
                adapter.notifyItemInserted(listOfTasks.size-1)

            // Reset the add task field
            inputTextField.clear()

            saveData()
        }
    }
    
    fun getDataFile(): File{
        return File(filesDir, "data.txt" )
    }

    fun loadData(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException:IOException){
            ioException.printStackTrace()
        }

    }

    fun saveData(){
        try {
            FileUtils.writeLines(getDataFile(),listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}