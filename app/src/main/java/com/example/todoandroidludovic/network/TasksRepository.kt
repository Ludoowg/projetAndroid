package com.example.todoandroidludovic.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoandroidludovic.network.models.Task

class TasksRepository {
    private val tasksWebService = Api.tasksWebService
    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList
    suspend fun refresh() {
    // Call HTTP (opération longue):
        Log.d("LUDO","refresh")
        val tasksResponse = tasksWebService.getTasks()
    // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            Log.d("LUDO","success")
            val fetchedTasks = tasksResponse.body()
            Log.d("LUDO",fetchedTasks?.size.toString())
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
        }
    }

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun removeTask(task: Task) : Boolean {
        val res= tasksWebService.deleteTask(task.id)
        return res.isSuccessful
    }
    suspend fun createTask(task: Task) : Task? {
        val res= tasksWebService.createTask(task)
        return if (res.isSuccessful) res.body() else null
    }
    suspend fun updateTask(task: Task) : Task? {
        val res= tasksWebService.updateTask(task,task.id)
        return if (res.isSuccessful) res.body() else null
    }

    suspend fun updateTask1(task: Task) {
        val res= tasksWebService.updateTask(task,task.id)
        if(res.isSuccessful){
            var updatedTask=res.body()
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { updatedTask!!.id == it.id }
        editableList[position] = updatedTask!!
        _taskList.value = editableList
        }
    }

    suspend fun addTask(task: Task) {
        val res= tasksWebService.createTask(task)
        if(res.isSuccessful){
            var addedTask=res.body()
            val editableList = _taskList.value.orEmpty().toMutableList()
            addedTask?.let {
                editableList.add(it)
            }
            _taskList.value = editableList
        }
    }

    suspend fun deleteTask(task: Task) {
        val res= tasksWebService.deleteTask(task.id)
        if(res.isSuccessful){
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.remove(task)
            _taskList.value = editableList
        }
    }
}