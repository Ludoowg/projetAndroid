package com.example.todoandroidludovic.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todoandroidludovic.R
import com.example.todoandroidludovic.network.models.Task
import kotlinx.android.synthetic.main.task_activity_layout.*
import java.util.*


class TaskActivity : AppCompatActivity() { //Déclaration manifest ?

    companion object {
        const val ADD_TASK_REQUEST = 666
        const val TASK_KEY = "reply_key"
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_activity_layout)

        var task = intent.getParcelableExtra(TASK_KEY) as? Task

        task?.let {
            buttonOne.text = "Modifier"
            titleTxt.setText(task.title)
            descTxt.setText(task.description)
        }

        buttonOne.setOnClickListener {
            if (titleTxt.text.isNotEmpty() && descTxt.text.isNotEmpty()) {

                val newTask = Task(id = task?.id ?:  UUID.randomUUID().toString(), title = titleTxt.text.toString(), description = descTxt.text.toString())
                intent.putExtra(TASK_KEY, newTask)
                setResult(RESULT_OK, intent) //Pas encore défini de RESULT_OK ?
                finish()
            }
            else {
                if(titleTxt.text.isEmpty()){
                    titleTxt.error = "Title must be not empty"
                        }
                if(descTxt.text.isEmpty()){
                    descTxt.error = "Description must be not empty"
                }
            }
        }
    }
}







