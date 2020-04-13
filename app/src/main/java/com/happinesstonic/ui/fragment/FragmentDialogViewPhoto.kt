package com.happinesstonic.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.happinesstonic.*
import com.happinesstonic.utils.*

import com.happinesstonic.viewmodel.ModelHomeActivity
import kotlinx.android.synthetic.main.fragment_dialog_view_photo.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.HashMap


class FragmentDialogViewPhoto : DialogFragment() {
    lateinit var thisContext: Activity
    lateinit var modelHomeActivity: ModelHomeActivity
    lateinit var listDataBinder: TextClassBinder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_view_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisContext = activity!!

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor = Color.parseColor("#000000")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor = ContextCompat.getColor(thisContext, R.color.colorPrimaryDark)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        modelHomeActivity = activity!!.run{
            ViewModelProvider(this).get(ModelHomeActivity::class.java)
        }

        val data = Gson().fromJson(
            ClassSharedPreferences(
                thisContext
            ).getCurrentDataBinder(), Array<TextClassBinder>::class.java).asList()
        if (data.isEmpty() ||data[0].joke_type !="image"){
            dialog!!.dismiss()
            ClassAlertDialog(thisContext).toast("Error occurred, Try again...")
            return
        }
        listDataBinder = data[0]

        LoadImg(thisContext, joke_photo).execute(listDataBinder.joke_image_url)


        joke_photo.setOnClickListener {
            if(frag_toolbar.visibility == View.VISIBLE){
                frag_toolbar.visibility = View.GONE
            }else{
                frag_toolbar.visibility = View.VISIBLE
            }
        }


        if ((ClassSharedPreferences(context)
                .isUserAdmin()||listDataBinder.joke_user_id == ClassSharedPreferences(
                context
            ).getUserId()))
            img_delete_btn.visibility = View.VISIBLE else img_delete_btn.visibility = View.GONE

        go_back.setOnClickListener {
            dialog!!.dismiss()
        }
        img_whatsapp_share.setOnClickListener{
            shareImage(listDataBinder, "whatsapp")
        }
        img_share_btn.setOnClickListener{
            shareImage(listDataBinder, "others")
        }
        img_delete_btn.setOnClickListener{
            AlertDialog.Builder(context)
                .setMessage("Delete this Photo?")
                .setPositiveButton("Delete"
                ) { _, _ ->
                    delPost(listDataBinder)
                }.setNegativeButton("cancel"
                ) { _, _ ->
                }.show()
        }
    }

    private fun shareImage(listDetail: TextClassBinder, shareTo:String){
        val fileName = ClassUtilities().getDirPath(thisContext, "pic")+"/${ClassUtilities().getImageName(listDetail)}"

        if(File(fileName).exists()){
            ClassShareApp(thisContext)
                .shareFileFromPath(fileName,"$shareTo")
        }else{
            listDetail.share_to = shareTo
            modelHomeActivity.setDownloadJokeFile(listDataBinder)
        }

    }


    private fun delPost(listDetail: TextClassBinder, position: Int=1) {
        val pDialog = ClassProgressDialog(context)
        pDialog.createDialog()

        val stringRequest = object : StringRequest(
            Method.POST, UrlHolder.URL_EDIT_JOKE,
            com.android.volley.Response.Listener { response ->
                pDialog.dismissDialog()

                try {
                    val obj = JSONObject(response)
                    val responseStatus = obj.getString("message")
                    if (responseStatus =="ok") {

                        dialog!!.dismiss()
//                        list.removeAt(position)
//                        notifyItemRemoved(position)
                    }else{
                        ClassAlertDialog(thisContext).toast(responseStatus)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            com.android.volley.Response.ErrorListener { _ ->
                pDialog.dismissDialog()
                ClassAlertDialog(thisContext).toast("Post couldn't be deleted (NETWORK ERROR!)")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val params = HashMap<String, String?>()
                params["request_type"] = "delete_post"
                params["joke_id"] = "${listDetail.joke_id}"
                return params
            }
        }
        //adding request to queue
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
        //volley interactions end
    }





    override fun onResume() {
        super.onResume()
        ClassUtilities().lockScreen(context)//Screen rotation...
    }

    override fun onPause() {
        super.onPause()
        ClassUtilities().unlockScreen(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        } else {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog.window!!.attributes.windowAnimations = R.style.Animation_WindowSlideUpDown
//        isCancelable = false
        return dialog
    }
}
