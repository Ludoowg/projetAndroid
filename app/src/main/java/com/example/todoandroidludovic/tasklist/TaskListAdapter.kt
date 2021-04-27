import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoandroidludovic.R
import com.example.todoandroidludovic.tasklist.Task
import kotlinx.android.synthetic.main.fragment_task_list.view.*
import kotlinx.android.synthetic.main.item_task.view.*
import java.util.*


class TaskListAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)

    }

    var onDeleteTask: ((Task) -> Unit)? = null

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(varTask: Task){

            itemView.apply {
                task_title.text = varTask.title
                buttonAndroid.setOnClickListener {
                    Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
                    notifyItemInserted(taskList.size -1)
                }

                buttonDelete.setOnClickListener {
                    onDeleteTask?.invoke(varTask)
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }
}


