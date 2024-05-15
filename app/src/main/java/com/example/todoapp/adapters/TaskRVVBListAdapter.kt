package com.example.todoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ViewTaskGridLayoutBinding
import com.example.todoapp.databinding.ViewTaskLayoutBinding
import com.example.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRVVBListAdapter(
    private val context:Context,
    private val isList: MutableLiveData<Boolean>,
    private val deleteUpdateCallback: (type: String, position: Int, task: Task) -> Unit,
) :
    ListAdapter<Task,RecyclerView.ViewHolder>(DiffCallback()) {



    class ListTaskViewHolder(private val viewTaskListLayoutBinding: ViewTaskLayoutBinding) :
        RecyclerView.ViewHolder(viewTaskListLayoutBinding.root) {

        fun bind(
            context: Context,
            task: Task,
            deleteUpdateCallback: (type: String, position: Int, task: Task) -> Unit,
        ) {
            viewTaskListLayoutBinding.titleTxt.text = task.title
            viewTaskListLayoutBinding.descrTxt.text = task.description

            val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault())

            viewTaskListLayoutBinding.dateTxt.text = dateFormat.format(task.date)

            // Get the task level
            val taskLevel = task.level
            // Set background color based on task level
            val backgroundColor = when (taskLevel) {
                "low" -> ContextCompat.getColor(context, R.color.low)
                "medium" -> ContextCompat.getColor(context, R.color.medium)
                "high" -> ContextCompat.getColor(context, R.color.high)
                else -> ContextCompat.getColor(context, R.color.mainBackGroundColor)
            }

            // Set background color to the MaterialCardView
            viewTaskListLayoutBinding.materialCardView.setCardBackgroundColor(backgroundColor)

            viewTaskListLayoutBinding.deleteImg.setOnClickListener {
                if (adapterPosition != -1) {
                    deleteUpdateCallback("delete", adapterPosition, task)
                }
            }
            viewTaskListLayoutBinding.editImg.setOnClickListener {
                if (adapterPosition != -1) {
                    deleteUpdateCallback("update", adapterPosition, task)
                }
            }
        }
    }


    class GridTaskViewHolder(private val viewTaskGridLayoutBinding: ViewTaskGridLayoutBinding) :
        RecyclerView.ViewHolder(viewTaskGridLayoutBinding.root) {

        fun bind(
            context: Context,
            task: Task,
            deleteUpdateCallback: (type: String, position: Int, task: Task) -> Unit,
        ) {
            viewTaskGridLayoutBinding.titleTxt.text = task.title
            viewTaskGridLayoutBinding.descrTxt.text = task.description

            val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault())

            viewTaskGridLayoutBinding.dateTxt.text = dateFormat.format(task.date)

            // Get the task level
            val taskLevel = task.level
            // Set background color based on task level
            val backgroundColor = when (taskLevel) {
                "low" -> ContextCompat.getColor(context, R.color.low)
                "medium" -> ContextCompat.getColor(context, R.color.medium)
                "high" -> ContextCompat.getColor(context, R.color.high)
                else -> ContextCompat.getColor(context, R.color.mainBackGroundColor)
            }

            // Set background color to the MaterialCardView
            viewTaskGridLayoutBinding.materialCardView1.setCardBackgroundColor(backgroundColor)

            viewTaskGridLayoutBinding.deleteImg.setOnClickListener {
                if (adapterPosition != -1) {
                    deleteUpdateCallback("delete", adapterPosition, task)
                }
            }
            viewTaskGridLayoutBinding.editImg.setOnClickListener {
                if (adapterPosition != -1) {
                    deleteUpdateCallback("update", adapterPosition, task)
                }
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return if (viewType == 1){  // Grid_Item
            GridTaskViewHolder(
                ViewTaskGridLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }else{  // List_Item
            ListTaskViewHolder(
                ViewTaskLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = getItem(position)

        if (isList.value!!){
            (holder as ListTaskViewHolder).bind(context,task,deleteUpdateCallback)
        }else{
            (holder as GridTaskViewHolder).bind(context,task,deleteUpdateCallback)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (isList.value!!){
            0 // List_Item
        }else{
            1 // Grid_Item
        }
    }



    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }

}