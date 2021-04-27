import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoandroidludovic.R
import com.example.todoandroidludovic.task.TaskActivity
import com.example.todoandroidludovic.tasklist.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.android.synthetic.main.fragment_task_list.view.*

class TaskListFragment : Fragment () {



    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", text_description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    private val adapter = TaskListAdapter(taskList)

    override fun onCreateView(
        inflater: LayoutInflater, //fichier xml, layout
        container: ViewGroup?, // vue parente qui contient le fragment
        savedInstanceState: Bundle? //fragment transmis par ce paramètre
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.onDeleteTask = { task ->
            taskList.remove(task)
            adapter.notifyDataSetChanged()
        }


        buttonAndroid.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            val ADD_TASK_REQUEST_CODE = 666
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val task = data?.getSerializableExtra(TaskActivity.TASK_KEY) as? Task

        if (requestCode == 666 && resultCode == Activity.RESULT_OK) {
            val reply = data!!.getStringExtra(SecondActivity.EXTRA_REPLY)

        }
    }
}




