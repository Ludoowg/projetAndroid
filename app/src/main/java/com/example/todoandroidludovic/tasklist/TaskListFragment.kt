package com.example.todoandroidludovic.tasklist

import TaskListAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.todoandroidludovic.R
import com.example.todoandroidludovic.network.models.Task
import com.example.todoandroidludovic.task.TaskActivity
import com.example.todoandroidludovic.task.TaskActivity.Companion.TASK_KEY
import com.example.todoandroidludovic.userinfo.UserInfoActivity
import kotlinx.android.synthetic.main.fragment_task_list.*
import java.util.*


class TaskListFragment : Fragment() {
    val ADD_TASK_REQUEST_CODE = 666

    private val viewModel: TaskListViewModel by viewModels()

    override fun onResume() {

        viewModel.loadTasks()
        viewModel.getUser()

        super.onResume()

    }


    private val adapter = TaskListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, //fichier xml, layout
        container: ViewGroup?, // vue parente qui contient le fragment
        savedInstanceState: Bundle? //fragment transmis par ce paramètre
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.user.observe(viewLifecycleOwner){ userInfo ->
            textView.text = "${userInfo.firstname} ${userInfo.lastname}"
            imageView.load(userInfo.avatar){
                transformations(CircleCropTransformation())
            }
        }
        viewModel.taskList.observe(viewLifecycleOwner) { newList ->
            adapter.submitList(newList)
        }
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.onDeleteTask = { task ->
            viewModel.deleteTask(task)
        }

        imageView.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivity(intent)
        }

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                if (it.resultCode == Activity.RESULT_OK) {
                    val task = it.data?.getParcelableExtra<Task>(TaskActivity.TASK_KEY) as Task
                    var indexOfTask = viewModel.taskList.value?.indexOfFirst { it.id == task.id }
                    if (indexOfTask == -1) {
                        viewModel.addTask(task)
                    } else {

                       viewModel.editTask(task)
                    }
                  //  adapter.submitList(taskList.toList())
                }
            }

        adapter.onEditTask = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TASK_KEY, task)

            startForResult.launch(intent)
        }



        buttonAndroid.setOnClickListener {


            val intent = Intent(activity, TaskActivity::class.java)


            startForResult.launch(intent)

        }


       if (activity?.intent?.action == Intent.ACTION_SEND) {
           if ("text/plain" == activity?.intent?.type) {

               val sharedText: String? = activity?.intent?.getStringExtra(Intent.EXTRA_TEXT)
               if (!sharedText.isNullOrEmpty()) {
                   var task = Task(UUID.randomUUID().toString(), sharedText, "")
                   val intent = Intent(activity, TaskActivity::class.java)
                   intent.putExtra(TASK_KEY, task)
                   startForResult.launch(intent)
               }
           }
       }

    }



}




