package com.happinesstonic.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import com.happinesstonic.R

class ClassProgressDialog(var context: Context?, var text:String?="Please Wait...", var cancelable:Boolean = false) {
    private var alertDialog:AlertDialog? = null
    var builder:AlertDialog.Builder = AlertDialog.Builder(context)
    var dialogView:View = LayoutInflater.from(context).inflate(R.layout.progress_dialog,null)
    var message:TextView

    init {
        //Alert Dialog declaration starts
        message = dialogView.findViewById(R.id.message)
        message.text = text
        builder.setView(dialogView)
        builder.setCancelable(cancelable)
        try {
            alertDialog = builder.create()
        } catch (e: Exception) {
        }
    }

    fun createDialog(){
//        ClassUtilities().lockScreen(context)
        try {
            alertDialog?.show()
        } catch (e: Exception) { e.printStackTrace() }
    }
    fun dismissDialog(){
//        try {
//            ClassUtilities().unlockScreen(context)
//        } catch (e: Exception) { }

        try {
            alertDialog?.dismiss()
        } catch (e: Exception) {e.printStackTrace()}
    }
    fun updateMsg(msg:String){
        message.text = msg
    }

}