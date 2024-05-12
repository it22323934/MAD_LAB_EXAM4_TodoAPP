package com.example.todoapp.utils

import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}

fun Context
    .LongToastShow(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}

fun Dialog.setupDialog(layoutResId:Int){
    setContentView(layoutResId)
    window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
    )
    setCancelable(false)
}

fun validateEditText(editText: EditText, textTextInputLayout: TextInputLayout): Boolean {
    return when {
        editText.text.toString().trim().isEmpty() -> {
            textTextInputLayout.error = "Required"
            false
        }
        else -> {
            textTextInputLayout.error = null
            true
        }
    }
}

fun validatePriority(priorityText: String, textTextInputLayout: TextInputLayout): Boolean {
    val trimmedPriority = priorityText.trim().lowercase()
    return when (trimmedPriority) {
        "low" -> {
            textTextInputLayout.error = null
            true
        }
        "medium" -> {
            textTextInputLayout.error = null
            true
        }
        "high" -> {
            textTextInputLayout.error = null
            true
        }
        else -> {
            textTextInputLayout.error = "Invalid priority"
            false
        }
    }
}


fun clearEditText(editText: EditText, textTextInputLayout: TextInputLayout){
    editText.text=null
    textTextInputLayout.error = null
}