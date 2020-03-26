package com.laughoutloud.ui.fragment


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.view.*
import android.widget.*
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.laughoutloud.*
import com.laughoutloud.ui.activity.ActivityCropImage
import com.laughoutloud.viewmodel.ModelHomeActivity
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.alert_dialog_inflate_choose_gallery.view.*
import kotlinx.android.synthetic.main.fragment_dialog_upload_video_joke.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.Logger


class FragmentDialogUploadVideoJoke: DialogFragment(){
    lateinit var thisContext: Activity
    lateinit var prefs: ClassSharedPreferences
    lateinit var modelHomeActivity: ModelHomeActivity


    var fileUri: Uri? = null
    var mediaPath: String? = null
    var mImageFileLocation = ""
    var postPath: String? = null
    var gifImgPath: String? = null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_upload_video_joke, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisContext = activity!!
        modelHomeActivity = ViewModelProvider(this).get(ModelHomeActivity::class.java)
        prefs = ClassSharedPreferences(thisContext)



        pickImage.setOnClickListener {
            getImageFromGallery()
        }
        //image gif wrapper
        imagePreviewWrapper.setOnClickListener {
//            showImageDialog()
        }
        removeVideo.setOnClickListener {
            removeImage()
        }

        submit_joke_image.setOnClickListener {
            uploadFile()//checking for the inputs
        }

    }





    // Uploading Image/Video
    private fun uploadFile() {
        if (postPath == null || postPath == "") {
            ClassAlertDialog(thisContext).toast("No Image selected...")
        } else {
            val dialog = ClassProgressDialog(thisContext, "Uploading, Please Wait...")
            dialog.createDialog()

            // Map is used to multipart the file using okhttp3.RequestBody
            val map = HashMap<String, RequestBody>()
            val file = File(postPath!!)

            // Parsing any Media type file
            val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
            map["file\"; filename=\"" + file.name + "\""] = requestBody
            map["gif_file\"; filename=\"" + File(gifImgPath!!).name + "\""] = requestBody//for gif file...

            val getResponse = com.laughoutloud.networking.AppConfig.getRetrofit().create(com.laughoutloud.networking.ApiConfigVideo::class.java)
            val call = getResponse.upload(
                "token",
                map,
                map,
                "image_upload",
                "${prefs.getUserDetails("id")}"
            )

            call.enqueue(object : Callback<com.laughoutloud.networking.ServerResponse> {
                override fun onResponse(call: Call<com.laughoutloud.networking.ServerResponse>, response: Response<com.laughoutloud.networking.ServerResponse>) {

                    dialog.dismissDialog()
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val serverResponse = response.body()

                            if (!serverResponse!!.success){
                                ClassAlertDialog(thisContext).toast("${serverResponse.message}")
                            }else{
                                ClassAlertDialog(thisContext).toast("Video uploaded successfully...")

                                refreshViewpager()
                            }
                        }
                    } else {
                        ClassAlertDialog(thisContext).toast("Error uploading video...")
                    }
                }

                override fun onFailure(call: Call<com.laughoutloud.networking.ServerResponse>, t: Throwable) {
                    dialog.dismissDialog()
                    ClassAlertDialog(thisContext).toast("Video couldn't be uploaded (NETWORK ERROR!)")
                }
            })
        }

    }

    private fun refreshViewpager() {
        modelHomeActivity.refreshJokeTab("video")
        resetInputs()
        dialog!!.dismiss()
    }


    override fun onResume() {
        super.onResume()
        if(postPath==null){
            removeImage()
        }

        //lock screen
        ClassUtilities().lockScreen(thisContext)//Lock Screen rotation...
    }

    override fun onPause() {
        super.onPause()
        ClassUtilities().unlockScreen(thisContext)//UnLock Screen rotation...
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // get the file url
        fileUri = savedInstanceState?.getParcelable("file_uri")
//        dialog.window!!.attributes.windowAnimations = R.style.Animation_WindowSlideUpDown
    }

    private fun gotoCropActivity(){
        startActivity(Intent(thisContext, ActivityCropImage::class.java))
        activity!!.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }
    private fun removeImage(){
        imagePreview?.setImageDrawable(null)
        imagePreviewWrapper?.visibility = View.GONE

        pickImage.visibility = View.VISIBLE
        postPath = null
    }

    private fun resetInputs(){//open function can be overridden
        removeImage()
    }


    private fun getImageFromGallery(){
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)//chaned From Images to Video
//            galleryIntent.type = "video/*"
//            galleryIntent.setType("video/mp4")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayListOf("image/jpeg","image/png","video/mp4","video/quicktime"))//quicktime is .mov extension
                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayListOf("video/mp4"))
            }
            startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            imagePreviewWrapper.visibility = View.VISIBLE

            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Get the Image from data
                    val selectedImage = data.data!!
                    val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)//From Images to Video

                    val cursor = activity!!.contentResolver.query(selectedImage, filePathColumn, null, null, null)!!
                    cursor.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
//                    OR
//                    val mediaPath2 = ClassUtilities().getFilePathFromURI(thisContext,selectedImage)
                    cursor.close()


                    postPath = mediaPath
                }

            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT <= 21) {
//                    Glide.with(this).load(fileUri).into(imagePreview)
                    postPath = fileUri!!.path

                } else {
//                    Glide.with(this).load(mImageFileLocation).into(imagePreview)
                    postPath = mImageFileLocation

                }

            }




            val postPathFile = File(postPath!!)
            val inputFileInKb = postPathFile.length()/1024//KB like 358BB
            if((postPath == null)||(postPathFile.length() <=3)){
                ClassAlertDialog(thisContext).toast("No Video selected...")
                removeImage()
                return
            }else if((inputFileInKb >= 1024*10)){//more than or equals 10MB
                ClassAlertDialog(thisContext).toast("Video size too large (MAX:10MB)")
                removeImage()
                return
            }

            thumbnailBackground().execute()
            pickImage.visibility = View.GONE

        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(thisContext, "Sorry, there was an error!", Toast.LENGTH_LONG).show()
        }
    }
    @SuppressLint("StaticFieldLeak")
    inner class thumbnailBackground : AsyncTask<Void, Void, Void?>(){
        private val pDialog = ClassProgressDialog(thisContext, "Generating Thumbnail, Please Wait...")

        init {
            pDialog.createDialog()
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try{
                val gifImg = encodeImg2Gif()

                if (gifImg!=null) Glide.with(thisContext).load(gifImg).into(imagePreview)
                else imagePreview.setImageResource(R.drawable.img_no_preview)
            }
            catch (e:InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            pDialog.dismissDialog()
        }


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageFromGallery()
            }else{
                ClassAlertDialog(thisContext).alertMessage("You must allow camera permission in order to take/pick a picture")
            }
        }
    }

    private fun encodeImg2Gif():String?{
        if (postPath==null)return null

        val imgBitmaps = getImgFromVideo(postPath!!)
        if (imgBitmaps.size == 0){
            ClassAlertDialog(thisContext).alertMessage("No bitmap gotten from the video file...")
            return null
        }else{
            val bos = ByteArrayOutputStream()
            val encoder = AnimatedGifEncoder()
            encoder.setDelay(500)
            encoder.setRepeat(0)
            encoder.start(bos)
            try{
                for (bmp in imgBitmaps){
                    encoder.addFrame(bmp) // gifに追加

                    if (bmp == imgBitmaps.last())
                        bmp.recycle()
                }
            }catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            encoder.finish() // 終了

            gifImgPath = ClassUtilities().getDirPath(thisContext)+"/.gif/"+ClassUtilities().assignGifName()
            val filePath = File(gifImgPath!!)
            val outputStream: FileOutputStream
            try{
                outputStream = FileOutputStream(filePath)
                outputStream.write(bos.toByteArray())
            }catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return gifImgPath
    }

    private fun getImgFromVideo(file_path: String): MutableList<Bitmap>{
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(thisContext, Uri.fromFile(File(file_path)))
//        retriever.setDataSource("/sdcard/LOL/test.mp4")
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val timeInSec = java.lang.Long.parseLong(time)/(1000)//video duration



        val imgThumbnailBitmaps = mutableListOf<Bitmap>()
        val randTime = (4 until 7).shuffled().last()
        val timeInterval = (timeInSec/randTime).toInt()

        var nextImgtime = timeInterval
        var timeCount = 0
        while(timeCount < (randTime-1)){//3,4,5 or 6 times

            var imgBitmap: Bitmap?=null
            try{
//                imgBitmap = retriever.getFrameAtTime(nextImgtime * 1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                imgBitmap = retriever.getFrameAtTime(TimeUnit.SECONDS.toMicros(nextImgtime.toLong()), MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            }catch (ex:Exception) {
                Log.i("imgBitmap_debug_error", "MediaMetadataRetriever got exception:$ex")
                ex.printStackTrace()
            }


            if(imgBitmap!=null){
                imgThumbnailBitmaps.add(imgBitmap)
            }

            nextImgtime +=timeInterval
            timeCount++
        }

        retriever.release()

        return imgThumbnailBitmaps
    }









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.Animation_WindowSlideUpDown)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        } else {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar)
        }
    }


    //
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.attributes.windowAnimations = R.style.Animation_WindowSlideUpDown
//        isCancelable = false
        return dialog
    }






    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val REQUEST_PICK_PHOTO = 2
        private const val CAMERA_PIC_REQUEST = 1111

        private val TAG = FragmentDialogUploadVideoJoke::class.java.simpleName

        private const val CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100

        const val MEDIA_TYPE_IMAGE = 1
        const val IMAGE_DIRECTORY_NAME = "Android File Upload"

        /**
         * returning image / video
         */
        private fun getOutputMediaFile(type: Int): File? {

            // External sdcard location
            val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME)

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create "
                            + IMAGE_DIRECTORY_NAME + " directory")
                    return null
                }
            }

            // Create a media file name
            //val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val mediaFile: File
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = File(mediaStorageDir.path + File.separator
                        + "IMG_" + ".jpg")
            } else {
                return null
            }

            return mediaFile
        }
    }
}
