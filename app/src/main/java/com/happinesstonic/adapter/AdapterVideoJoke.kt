package com.happinesstonic.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.happinesstonic.VolleySingleton
import com.happinesstonic.*
import com.happinesstonic.ui.fragment.FragmentDialogWatchVideo
import com.happinesstonic.utils.*
import com.happinesstonic.viewmodel.ModelHomeActivity
import kotlinx.android.synthetic.main.adapter_item_video.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.HashMap


class AdapterVideoJoke(val list:MutableList<TextClassBinder>, val context: Context) : RecyclerView.Adapter<AdapterVideoJoke.ViewHolder>() {


    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_video, parent, false)
        return ViewHolder(view)
    }



    fun addItems(items: MutableList<TextClassBinder>) {
        val lastPos = list.size - 1
        list.addAll(items)
        notifyItemRangeInserted(lastPos, items.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listDetail = list[position]

        LoadImg(context, holder.vid_picture).execute(listDetail.joke_image_url)


        if ((ClassSharedPreferences(context)
                .isUserAdmin()||listDetail.joke_user_id == ClassSharedPreferences(
                context
            ).getUserId()))
            holder.vid_delete_btn.visibility = View.VISIBLE else holder.vid_delete_btn.visibility = View.GONE


        holder.vid_delete_btn.setOnClickListener{
            AlertDialog.Builder(context)
                .setMessage("Delete this Video?")
                .setPositiveButton("Delete"
                ) { _, _ ->
                    delPost(listDetail, position)
                }.setNegativeButton("cancel"
                ) { _, _ ->
                }.show()
        }

        holder.vid_wrapper.setOnClickListener{
            ClassSharedPreferences(context).setCurrentDataBinder(Gson().toJson(mutableListOf(listDetail)))
            ClassSharedPreferences(context).setCurrentListBinder(Gson().toJson(list))

            //show video fragment dialog
            val videoDialogFragment = FragmentDialogWatchVideo()
            val ft = (context as FragmentActivity).supportFragmentManager.beginTransaction()
            val prev = context.supportFragmentManager.findFragmentByTag(FragmentDialogWatchVideo::class.java.name)
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            videoDialogFragment.show(ft, FragmentDialogWatchVideo::class.java.name)
        }

        holder.vid_whatsapp_share.setOnClickListener{
            shareVideo(listDetail, "whatsapp")
        }
        holder.vid_share_btn.setOnClickListener{
            shareVideo(listDetail, "others")
        }
    }

    private fun shareVideo(listDetail: TextClassBinder, shareTo:String){
        val fileName = ClassUtilities().getDirPath(context, "vid")+"/${ClassUtilities().getVideoName(listDetail)}"

        if(File(fileName).exists()){
            ClassShareApp(context)
                .shareFileFromPath(fileName,"$shareTo")
        }else{
            val modelHomeActivity = context.run{
                ViewModelProvider(context as ViewModelStoreOwner).get(ModelHomeActivity::class.java)
            }
            listDetail.share_to = shareTo
            modelHomeActivity.setDownloadJokeFile(listDetail)
        }

    }

    private fun delPost(listDetail: TextClassBinder, position: Int) {
        val pDialog = ClassProgressDialog(context)
        pDialog.createDialog()

        val stringRequest = object : StringRequest(
            Method.POST, UrlHolder.URL_EDIT_JOKE,
            com.android.volley.Response.Listener<String> {response ->
                pDialog.dismissDialog()

                try {
                    val obj = JSONObject(response)
                    val responseStatus = obj.getString("message")
                    if (responseStatus =="ok") {

                        list.removeAt(position)
                        notifyItemRemoved(position)

                    }else{
                        ClassAlertDialog(context).toast(responseStatus)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            com.android.volley.Response.ErrorListener { _ ->
                pDialog.dismissDialog()
                ClassAlertDialog(context).toast("Post couldn't be deleted (NETWORK ERROR!)")
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


    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val vid_wrapper = v.vid_wrapper!!
        val vid_picture = v.vid_picture!!
        val vid_whatsapp_share = v.vid_whatsapp_share!!
        val vid_share_btn = v.vid_share_btn!!
        val vid_delete_btn = v.vid_delete_btn
    }

}