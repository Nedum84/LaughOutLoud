package com.laughoutloud.ui.fragment



import android.Manifest
import android.app.Activity
import android.content.Context
import android.view.*
import android.widget.*
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.laughoutloud.*
import com.laughoutloud.ui.activity.ActivityCropImage
import com.laughoutloud.viewmodel.ModelHomeActivity
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.alert_dialog_inflate_choose_gallery.view.*
import kotlinx.android.synthetic.main.alert_dialog_inflate_preview_image.view.*
import kotlinx.android.synthetic.main.fragment_dialog_upload_image_joke.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger


class FragmentDialogUploadImageJoke: DialogFragment(){
    lateinit var thisContext: Activity
    lateinit var prefs: ClassSharedPreferences
    lateinit var modelHomeActivity: ModelHomeActivity


    var fileUri: Uri? = null
    var mediaPath: String? = null
    var mImageFileLocation = ""
    var postPath: String? = null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_upload_image_joke, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisContext = activity!!
        modelHomeActivity = ViewModelProvider(this).get(ModelHomeActivity::class.java)
        prefs = ClassSharedPreferences(thisContext)



        pickImage.setOnClickListener {
            imagePickerDialog()
        }
        //
        imagePreviewWrapper.setOnClickListener {
            showImageDialog()
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

            val getResponse = com.laughoutloud.networking.AppConfig.getRetrofit().create(com.laughoutloud.networking.ApiConfig::class.java)
            val call = getResponse.upload(
                "token",
                map,
                "add_joke_image",
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
                                ClassAlertDialog(thisContext).toast("Photo uploaded successfully...")

                                refreshViewpager()
                            }
                        }
                    } else {
                        ClassAlertDialog(thisContext).toast("Error uploading image, try again...")
                    }
                }

                override fun onFailure(call: Call<com.laughoutloud.networking.ServerResponse>, t: Throwable) {
                    dialog.dismissDialog()
                    ClassAlertDialog(thisContext).toast("Image couldn't be uploaded (NETWORK ERROR!)")
                }
            })
        }

    }

    private fun refreshViewpager() {
        modelHomeActivity.refreshJokeTab("image")
        resetInputs()
        dialog!!.dismiss()
    }


    override fun onResume() {
        super.onResume()
        if(prefs.getQImgUrl() !="") {
            postPath = prefs.getQImgUrl()

            val postPathFile = File(postPath!!)
            val inputFileInMb = postPathFile.length()/(1024*1024)//MB like 27MB
            val inputFileInKb = postPathFile.length()/1024//KB like 358BB
            if((postPath == null)||(postPathFile.length() <=3)){
                ClassAlertDialog(thisContext).toast("No Image selected...")
                removeImage()
                return
            }else if((inputFileInKb >= 500)){//more than or equals 500KB
                postPath = compressAndAssignPath(postPathFile)//resizing image...
            }

            imagePreviewWrapper.visibility = View.VISIBLE
            pickImage.visibility = View.GONE
            Glide.with(this).load(postPath).into(imagePreview)

        }else{
            removeImage()
        }

        //lock screen
        ClassUtilities().lockScreen(thisContext)//Lock Screen rotation...
    }

    override fun onPause() {
        super.onPause()
        ClassUtilities().unlockScreen(thisContext)//UnLock Screen rotation...
    }
    private fun compressAndAssignPath(postPathFile:File) :String?{
        val dirPath = ClassUtilities().getDirPath(thisContext)
        val filePath = "$dirPath/${postPath!!.split("/").last()}"
        try {
            Compressor(thisContext)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(dirPath)
                .compressToFile(postPathFile)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return filePath
    }


    fun showImageDialog(){
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val inflater = LayoutInflater.from(context).inflate(R.layout.alert_dialog_inflate_preview_image, null)
        val builder = AlertDialog.Builder(thisContext)

        if(postPath!=null){
            Glide.with(this).load(postPath).into(inflater.dialogPreviewImage)
        }

        builder.setView(inflater)
        builder.setPositiveButton("Crop"
        ) { _, _ ->
            //actions
            ClassSharedPreferences(context).setQImgUrl(postPath!!)
            gotoCropActivity()
        }
        builder.setNeutralButton("Remove"
        ) { _, _ ->
            //actions
            removeImage()
        }

        val alertDialog = builder.create()
        alertDialog.show()

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
        prefs.setQImgUrl("")
    }

    fun resetInputs(){//open function can be overridden
        removeImage()
    }


    fun imagePickerDialog(){
        ClassUtilities().hideKeyboard(imagePreviewWrapper,thisContext)

        val inflater = LayoutInflater.from(context).inflate(R.layout.alert_dialog_inflate_choose_gallery, null)
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Choose Photo")
        builder.setView(inflater)
        val dialogImgPicker = builder.create()
        dialogImgPicker.show()


        //select from gallery
        inflater.fromGallery.setOnClickListener {
            dialogImgPicker.dismiss()
            if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            } else {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO)
            }
        }
        //select from capture
        inflater.fromCapture.setOnClickListener {
            dialogImgPicker.dismiss()
            // start the image capture Intent
            if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            } else {
                captureImage()
            }

        }
        //remove image
        inflater.removeImage.setOnClickListener {
            dialogImgPicker.dismiss()
            removeImage()
        }

    }





    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage()
            }else{
                ClassAlertDialog(thisContext).alertMessage("You must allow camera permission in order to take/pick a picture")
            }
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
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = activity!!.contentResolver.query(selectedImage, filePathColumn, null, null, null)!!
                    cursor.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
//                    OR
//                    val mediaPath2 = ClassUtilities().getFilePathFromURI(thisContext,selectedImage)
                    // Set the Image in ImageView for Previewing the Media
                    val options = BitmapFactory.Options()//additional parameter
                    options.inSampleSize = 2//additional parameter

                    imagePreview.setImageBitmap(BitmapFactory.decodeFile(mediaPath,options))
                    cursor.close()


                    postPath = mediaPath
                }

            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT <= 21) {
                    Glide.with(this).load(fileUri).into(imagePreview)
                    postPath = fileUri!!.path

                } else {

                    Glide.with(this).load(mImageFileLocation).into(imagePreview)
                    postPath = mImageFileLocation

                }

            }





//            Handler(Looper.getMainLooper()).postDelayed({
                resizeAndGotoCrop()
//            }, 1500) //Delay 1.5 second

        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(thisContext, "Sorry, there was an error!", Toast.LENGTH_LONG).show()
        }
    }



    private fun resizeAndGotoCrop() {
        Thread.sleep(1200)
        val postPathFile = File(postPath!!)
        val inputFileInKb = postPathFile.length()/1024//KB like 358BB
        if((postPath == null)||(postPathFile.length() <=3)){
            ClassAlertDialog(thisContext).toast("No Image selected...")
            prefs.setQImgUrl("")
            return
        }else if((inputFileInKb >= 500)){//more than or equals 500KB
            postPath = compressAndAssignPath(postPathFile)//resizing image...
        }


        prefs.setQImgUrl(postPath!!)
        gotoCropActivity()
        pickImage.visibility = View.GONE
    }

    /**
     * Launching camera app to capture image
     */

    @Throws(IOException::class)
    internal fun createImageFile(): File {
        Logger.getAnonymousLogger().info("Generating the image - method started")

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.getDefault()).format(Date())
        val imageFileName = "IMAGE_" + timeStamp
        // Here we specify the environment location and the exact path where we want to save the so-created file
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app")
        Logger.getAnonymousLogger().info("Storage directory set")

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir()

        // Here we create the file using a prefix, a suffix and a directory
        val image = File(storageDirectory, "$imageFileName.png")
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set")

        mImageFileLocation = image.absolutePath
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image
    }
    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }
    private fun captureImage() {
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            val callCameraApplicationIntent = Intent()
            callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE

            // We give some instruction to the intent to save the image
            var photoFile: File? = null

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile()
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (e: IOException) {
                Logger.getAnonymousLogger().info("Exception error in generating the file")
                e.printStackTrace()
            }

            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            val outputUri = FileProvider.getUriForFile(
                thisContext,
                BuildConfig.APPLICATION_ID + ".provider",
                photoFile!!)
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)

            Logger.getAnonymousLogger().info("Calling the camera App by intent")

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

            startActivityForResult(intent, CAMERA_PIC_REQUEST)
        }


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
//        dialog.window!!.attributes.windowAnimations = R.style.Animation_WindowSlideUpDown
//        isCancelable = false
        return dialog
    }



    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val REQUEST_PICK_PHOTO = 2
        private const val CAMERA_PIC_REQUEST = 1111

        private val TAG = FragmentDialogUploadImageJoke::class.java.simpleName

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
