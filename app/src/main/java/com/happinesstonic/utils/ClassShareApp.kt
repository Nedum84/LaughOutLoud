package com.happinesstonic.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.happinesstonic.BuildConfig
import com.happinesstonic.R
import com.happinesstonic.UrlHolder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ClassShareApp(val context: Context) {

    private val appPackageName: String? = context.packageName // getPackageName() from Context or Activity object
    val intent: Intent = Intent()
    private val shareAppMsg: String = "Get \"${context.getString(R.string.app_name)}\" on Google Play via ${context.getString(R.string.app_play_url)} & keep being happy"
    private val shareAppMsg2: String =
        "Get \"${context.getString(R.string.app_name)}\" on Google Play Store via \n" +
                "https://play.google.com/store/apps/details?id=$appPackageName & keep laughing"

    init {
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
    }

    fun shareApp() {
        intent.putExtra(Intent.EXTRA_TEXT, shareAppMsg)
        startActivity(context, Intent.createChooser(intent, "Share on: "), Bundle())
    }





    fun shareText(msgTxt: String, shareTo:String = "whatsapp") {
        intent.putExtra(Intent.EXTRA_TEXT, msgTxt)

        if(shareTo=="whatsapp")
            intent.setPackage("com.whatsapp")
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null)
            context.startActivity(intent)
        }
    }

    fun shareImg1(imageView: ImageView, imgName:String, ctx:Context){
        val bmpUri = getLocalBitmapUri(imageView, imgName, ctx)
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
            shareIntent.type = "image/*"
            // Launch sharing dialog for image
            startActivity(context, Intent.createChooser(shareIntent, "Share Image"), Bundle())
        } else {
            // ...sharing failed, handle error
//            try to download the file


        }
    }
    private fun getLocalBitmapUri(imageView: ImageView, imgName:String, ctx:Context): Uri? {
        // Extract Bitmap from ImageView drawable
        val drawable = imageView.drawable
        var bmp: Bitmap? = null
        bmp = if (drawable is BitmapDrawable) {
            (imageView.drawable as BitmapDrawable).bitmap
        } else {
            return null
        }
        // Store image to default external storage directory
        var bmpUri: Uri? = null
        val fileName = ClassUtilities()
            .getRootDirPath(ctx) + "/"+ UrlHolder.APP_FOLDER_NAME +"/LOL_Photos/$imgName"
        try {
            if (!File(fileName).exists()){
                val file = File(fileName)
                file.parentFile!!.mkdirs()
                val out = FileOutputStream(file)
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
                out.close()
                bmpUri = Uri.fromFile(file)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    fun shareImg2(imageVIew:ImageView, shareTo:String = "whatsapp" ){
        val bitmapDrawable = imageVIew.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val bitmapPath = Images.Media.insertImage(context.contentResolver, bitmap, "Image  Title", null)
        val bitmapUri = Uri.parse(bitmapPath)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, "more @ bit.ly/rerere")


        if(shareTo=="whatsapp")
            shareIntent.setPackage("com.whatsapp")
        try {
            context.startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            // Chrome browser presumably not installed so allow user to choose instead
            shareIntent.setPackage(null)
            context.startActivity(Intent.createChooser(shareIntent, "Share Image to:"))
        }
    }

    fun share3(){
        val imgUri = Uri.parse("pictureFile.getAbsolutePath()")
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share")
        whatsappIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject text")
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri)
        whatsappIntent.type = "image/*"
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            context.startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
//            ToastHelper.MakeShortText("Whatsapp have not been installed.")
        }
    }
    fun shareFileFromPath(filePath:String, shareTo:String = "whatsapp"){
//        val uri = Uri.parse("path")//Uri.fromFile("v")//Uri.parse(path);
        val uri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".provider",
            File(filePath))


        val fileShareIntent = Intent(Intent.ACTION_SEND)
        fileShareIntent.type = "*/*"
        fileShareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        fileShareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        fileShareIntent.putExtra(Intent.EXTRA_TEXT, "more @ ${context.getString(R.string.app_play_url)}")


        if(shareTo=="whatsapp")
            fileShareIntent.setPackage("com.whatsapp")
        try {
            context.startActivity(fileShareIntent)
        } catch (ex: ActivityNotFoundException) {
            // Chrome browser presumably not installed so allow user to choose instead
            fileShareIntent.setPackage(null)
            context.startActivity(Intent.createChooser(fileShareIntent, "Share to:"))
        }
    }
}