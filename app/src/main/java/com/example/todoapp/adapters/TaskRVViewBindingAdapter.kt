package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ViewTaskLayoutBinding
import com.example.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRVViewBindingAdapter (
    private val deleteUpdateCallBack:(type:String,position:Int,task:Task)->Unit
): RecyclerView.Adapter<TaskRVViewBindingAdapter.ViewHolder>() {

    private val taskList = arrayListOf<Task>()

    class ViewHolder(val viewTaskLayoutBinding:ViewTaskLayoutBinding) :
        RecyclerView.ViewHolder(viewTaskLayoutBinding.root)

    fun addAllTask(newTaskList:List<Task>){
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ViewTaskLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: TaskRVViewBindingAdapter.ViewHolder, position: Int) {
        val task=taskList[position]
        holder.viewTaskLayoutBinding.titleTxt.text=task.title
        holder.viewTaskLayoutBinding.descrTxt.text=task.description

        val dataFormat=SimpleDateFormat("dd-MM-yyyy HH:mm:ss a", Locale.getDefault())
        holder.viewTaskLayoutBinding.dateTxt.text=dataFormat.format(task.date)

        holder.viewTaskLayoutBinding.deleteImg.setOnClickListener {
            if(holder.adapterPosition!=-1){
                deleteUpdateCallBack("delete",holder.adapterPosition,task)
            }
        }

        holder.viewTaskLayoutBinding.editImg.setOnClickListener {
            if(holder.adapterPosition!=-1){
                deleteUpdateCallBack("update",holder.adapterPosition,task)
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

}