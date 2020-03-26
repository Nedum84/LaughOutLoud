package com.laughoutloud

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import android.content.ClipData
import android.content.ClipboardManager
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.laughoutloud.viewmodel.ModelHomeActivity
import java.io.*


class ClassUtilities() {

    fun getRootDirPath(context: Context): String? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(context.applicationContext, null)[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }
    fun createDirs(context: Context) {
        var dir1: File? = null
        var dir2: File? = null
        var dir3: File? = null
        var dir4: File? = null
        var dir5: File? = null
//        val extStorageDir = context.getExternalFilesDir(null)
        val extStorageDir = context.getExternalFilesDir(getRootDirPath(context))
        if (extStorageDir!!.canWrite()) {
            dir1 = File(extStorageDir.path + "/"+UrlHolder.APP_FOLDER_NAME)
            dir2 = File(extStorageDir.path + "/"+UrlHolder.APP_FOLDER_NAME+"/LOL_Videos")
            dir3 = File(extStorageDir.path + "/"+UrlHolder.APP_FOLDER_NAME+"/LOL_Photos")
            dir4 = File(extStorageDir.path + "/"+UrlHolder.APP_FOLDER_NAME+"/.gif")
            dir5 = File(extStorageDir.path + "/"+UrlHolder.APP_FOLDER_NAME+"/.tmp")
        }
        if (dir1 != null) {
            if (!dir1.exists()) dir1.mkdirs()
            if (!dir2!!.exists()) dir2.mkdirs()
            if (!dir3!!.exists()) dir3.mkdirs()
            if (!dir4!!.exists()) dir4.mkdirs()
            if (!dir5!!.exists()) dir5.mkdirs()
        }
    }

    fun getDirPath(context: Context): String {
        var dirPath = ""
        var imageDir: File? = null
        val extStorageDir = context.getExternalFilesDir(null)
        if (extStorageDir!!.canWrite()) {
            imageDir = File(extStorageDir.path + "/"+UrlHolder.APP_FOLDER_NAME)
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.path
            }
        }
        return dirPath
    }

    fun assignImgName():String{
        val currentTimeMillis = System.currentTimeMillis()
        val today = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val title = dateFormat.format(today)
        return "lol_$title.jpg"
    }
    fun assignGifName():String{
        val currentTimeMillis = System.currentTimeMillis()
        val today = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val title = dateFormat.format(today)
        return "lol_$title.gif"
    }

    fun calculateNoOfColumns(context:Context, columnWidthDp:Float):Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val noOfColumns = (screenWidthDp / columnWidthDp + 0.5).toInt() // +0.5 for correct rounding to int.
//        return noOfColumns
        return noOfColumns-1
    }


    @SuppressLint("SourceLockedOrientationActivity")
    fun lockScreen(context: Context?){
        //lock screen
        val orientation = context!!.resources.configuration.orientation//activity!!.requestedOrientation(to get 10 more values)
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            try {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } catch (e: Exception) {}
        } else {
            // code for landscape mode
            try {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } catch (e: Exception) {}
        }
        //lock screen
//        OR
//        activity!!.requestedOrientation = orientation
        //OR
//        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }
    fun unlockScreen(context: Context?){
        try {
            (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } catch (e: Exception) {}
    }

    fun hideKeyboard(view: View?,activity: Activity){
//        val view = currentFocus
        try {
            if(view != null){
                val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
            activity.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        } catch (e: Exception) {}
    }

    fun copyToClipBoard(str:String,ctx:Context){
    val clipboard =  ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("text_label", str.trim())
    clipboard.setPrimaryClip(clip)
    }

    fun viewModelTest(ctx:ViewModelStoreOwner){

        val modelHomeActivity = ViewModelProvider(ctx).get(ModelHomeActivity::class.java)



//
//        //sender (string)
//        modelHomeActivity.myMessage("Hello! I am your message")
//        //receiver (string)
//        modelHomeActivity.message.observe((ctx as LifecycleOwner), object : Observer<Any> {
//            override fun onChanged(o: Any?) {
////                tv_msg.text = o?.toString()
//            }
//        })
//
//
//        modelHomeActivity.myMessage2("Hellow man...!!!")
//        modelHomeActivity.message2.observe(ctx, Observer {
//            it?.getContentIfNotHandled()?.let { msg_String ->
////                navigateToMediaItem(msg_String)
//            }
//        })
//
//        modelHomeActivity.navigateToMediaItem("Hellow man...!!!")
//        modelHomeActivity.navigateToMediaItem.observe(this, Observer {
//            it?.getContentIfNotHandled()?.let { msg_String ->
////                navigateToMediaItem(msg_String)
//            }
//        })
//
//        modelHomeActivity.textBinder("Hellow man...!!!")
//        modelHomeActivity.textBinder.observe(this, Observer {
//            it?.getContentIfNotHandled()?.let { msg_String ->
////                navigateToMediaItem(msg_String)
//            }
//        })


    }














    @SuppressLint("NewApi")
    fun getFilePathFromURI(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat) {
            if(DocumentsContract.isDocumentUri(context, uri)){

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            }
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}