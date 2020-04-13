package com.happinesstonic.ui.fragment


import android.app.Activity
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
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.happinesstonic.VolleySingleton
import com.happinesstonic.*
import com.happinesstonic.utils.ClassAlertDialog
import com.happinesstonic.utils.ClassProgressDialog
import com.happinesstonic.utils.ClassSharedPreferences
import com.happinesstonic.utils.ClassUtilities
import com.happinesstonic.viewmodel.ModelHomeActivity
import kotlinx.android.synthetic.main.fragment_dialog_create_joke.*
import org.json.JSONException
import org.json.JSONObject


class FragmentDialogCreateJoke : DialogFragment() {
    lateinit var thisContext: Activity
    lateinit var modelHomeActivity: ModelHomeActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_create_joke, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        modelHomeActivity = activity!!.run{
            ViewModelProvider(this).get(ModelHomeActivity::class.java)
        }

        submit_joke_text.setOnClickListener {
            val jokeTextMsg = joke_text_body.text.toString().trim()
            if (jokeTextMsg.isEmpty()){
                ClassAlertDialog(thisContext).toast("Enter joke...")
            }else{
                submitJoke(jokeTextMsg)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisContext = activity!!

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor = Color.parseColor("#F2F2F2")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor = ContextCompat.getColor(thisContext, R.color.colorPrimaryDark)
        }
    }

    private fun submitJoke(jokeTextMsg: String) {
        val progressDialog =
            ClassProgressDialog(thisContext)
        progressDialog.createDialog()

        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST, UrlHolder.URL_SEND_JOKE_TEXT,
            Response.Listener { response ->
                progressDialog.dismissDialog()

                try {
                    val obj = JSONObject(response)
                    val resMsg = obj.getString("message")
                    if (resMsg == "ok") {


                        joke_text_body.setText("")
                        modelHomeActivity.setRefreshJokeTab("text")
                        dialog!!.dismiss()
                    } else {
                        ClassAlertDialog(thisContext).toast( resMsg)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {er ->
                er.printStackTrace()
                progressDialog.dismissDialog()
                ClassAlertDialog(thisContext).toast("Error occurred while loading Answers. CHECK YOUR INTERNET CONNECTION!")
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val params = java.util.HashMap<String, String?>()
                params["request_type"] = "add_joke_text"
                params["joke_text_msg"] = jokeTextMsg
                params["user_id"] = ClassSharedPreferences(
                    thisContext
                ).getUserId()


                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)//adding request to queue
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

