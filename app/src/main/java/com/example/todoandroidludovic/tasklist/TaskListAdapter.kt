
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoandroidludovic.R
import com.example.todoandroidludovic.network.models.Task
import kotlinx.android.synthetic.main.item_task.view.*


class TaskListAdapter : ListAdapter<Task,TaskListAdapter.TaskViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)

    }




    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem:Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem:Task, newItem: Task): Boolean {
            return oldItem == newItem

        }}

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position),position)
    }

    var onDeleteTask: ((Task) -> Unit)? = null

    var onEditTask: ((Task) -> Unit)? = null

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(varTask: Task,position:Int){

            itemView.apply {
                task_title.text = varTask.title
                buttonDelete.setOnClickListener {
                    onDeleteTask?.invoke(varTask)
                }
                buttonEdit.setOnClickListener{
                    onEditTask?.invoke(varTask)
                }
            }
        }

    }


}


