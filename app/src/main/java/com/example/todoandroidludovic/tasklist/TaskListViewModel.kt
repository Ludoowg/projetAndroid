package com.example.todoandroidludovic.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoandroidludovic.network.TasksRepository
import com.example.todoandroidludovic.network.UserInfoRepository
import com.example.todoandroidludovic.network.models.Task
import com.example.todoandroidludovic.network.models.User
import kotlinx.coroutines.launch

class TaskListViewModel: ViewModel() {
    private val repository = TasksRepository()
    private val userRepository = UserInfoRepository()

    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList
    private val _user = MutableLiveData<User>()
    public val user: LiveData<User> = _user

    fun getUser() {
        viewModelScope.launch {
            val user = userRepository.getUser()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            user?.let{
                _user.value = it
            }
        }
    }
    fun loadTasks() {
        viewModelScope.launch {
                val fetchedTasks = repository.loadTasks()
                // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            fetchedTasks?.let{
                _taskList.value = it
            }
        }
    }
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            var deleteOK=repository.removeTask(task)
            if(deleteOK) {
                val editableList = _taskList.value.orEmpty().toMutableList()
                editableList.remove(task)
                _taskList.value = editableList
            }
        }}

    fun addTask(task: Task) {
        viewModelScope.launch {
            var addedTask=repository.createTask(task)
            val editableList = _taskList.value.orEmpty().toMutableList()
            addedTask?.let {
                editableList.add(it)
            }
            _taskList.value = editableList
        }
        }

    fun editTask(task: Task) {
        viewModelScope.launch {
            var updatedTask=repository.updateTask(task)
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { updatedTask!!.id == it.id }
            editableList[position] = updatedTask!!
            _taskList.value = editableList
        }
    }
}

