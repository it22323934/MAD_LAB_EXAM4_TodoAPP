package com.example.todoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ViewTaskLayoutBinding
import com.example.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRVVBListAdapter(
    private val context: Context,
    private val deleteUpdateCallBack: (type: String, position: Int, task: Task) -> Unit
) : ListAdapter<Task,TaskRVVBListAdapter.ViewHolder>(DiffCallBack()) {



    inner class ViewHolder(val viewTaskLayoutBinding: ViewTaskLayoutBinding) :
        RecyclerView.ViewHolder(viewTaskLayoutBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewTaskLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task =getItem(position)
        val binding = holder.viewTaskLayoutBinding

        binding.titleTxt.text = task.title
        binding.descrTxt.text = task.description

        val dataFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss a", Locale.getDefault())
        binding.dateTxt.text = dataFormat.format(task.date)

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
        binding.materialCardView.setCardBackgroundColor(backgroundColor)

        binding.deleteImg.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                deleteUpdateCallBack("delete", holder.adapterPosition, task)
            }
        }

        binding.editImg.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                deleteUpdateCallBack("update", holder.adapterPosition, task)
            }
        }
    }


    class DiffCallBack:DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
         return  oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
         return  oldItem==newItem
        }

    }
}
