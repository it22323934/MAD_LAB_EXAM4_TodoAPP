package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRecyclerViewAdapter (
    private val deleteUpdateCallBack:(type:String,position:Int,task:Task)->Unit
): RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    private val taskList = arrayListOf<Task>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt:TextView=itemView.findViewById(R.id.titleTxt)
        val descrTxt:TextView=itemView.findViewById(R.id.descrTxt)
        val dateTxt:TextView=itemView.findViewById(R.id.dateTxt)

        val deleteImg:ImageView=itemView.findViewById(R.id.deleteImg)
        val editImg:ImageView=itemView.findViewById(R.id.editImg)
    }

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
            LayoutInflater.from(parent.context).inflate(com.example.todoapp.R.layout.view_task_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TaskRecyclerViewAdapter.ViewHolder, position: Int) {
        val task=taskList[position]
        holder.titleTxt.text=task.title
        holder.descrTxt.text=task.description

        val dataFormat=SimpleDateFormat("dd-MM-yyyy HH:mm:ss a", Locale.getDefault())
        holder.dateTxt.text=dataFormat.format(task.date)

        holder.deleteImg.setOnClickListener {
            if(holder.adapterPosition!=-1){
                deleteUpdateCallBack("delete",holder.adapterPosition,task)
            }
        }

        holder.editImg.setOnClickListener {
            if(holder.adapterPosition!=-1){
                deleteUpdateCallBack("update",holder.adapterPosition,task)
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

}