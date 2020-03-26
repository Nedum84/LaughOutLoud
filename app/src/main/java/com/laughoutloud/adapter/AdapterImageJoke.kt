package com.laughoutloud.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.laughoutloud.ClassDateAndTime
import com.laughoutloud.VolleySingleton
import com.laughoutloud.*
import kotlinx.android.synthetic.main.adapter_item_image.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class AdapterImageJoke(val list:MutableList<TextClassBinder>, val context: Context) : RecyclerView.Adapter<AdapterImageJoke.ViewHolder>() {
    private var adapterCallbackInterface: ImageJokeAdapterCallbackInterface? = null


    init {
        setHasStableIds(true)
        try {
            adapterCallbackInterface = context as ImageJokeAdapterCallbackInterface
        } catch (e: ClassCastException) {
//            throw RuntimeException(context.toString() + "Activity must implement ImageJokeAdapterCallbackInterface.", e)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    //    override fun getItemId(position: Int)=position.toLong()
    override fun getItemId(position: Int): Long {
//        return list[position].question_id.toLong()
        return position.toLong()
//        return super.getItemId(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_text, parent, false)
        return ViewHolder(view)
    }



    fun addItems(items: MutableList<TextClassBinder>) {
        val lastPos = list.size - 1
        list.addAll(items)
        notifyItemRangeInserted(lastPos, items.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listDetail = list[position]

        LoadImg(context, holder.img_picture).execute(listDetail.joke_image_url)

        if ((ClassSharedPreferences(context).getUserDetails("user_level")!!.toInt()>1||listDetail.joke_user_id == ClassSharedPreferences(context).getUserDetails("id")))
            holder.img_delete_btn.visibility = View.VISIBLE else holder.img_delete_btn.visibility = View.GONE


        holder.img_delete_btn.setOnClickListener{
            delPost(listDetail, position)
        }

        holder.img_wrapper.setOnClickListener{
            adapterCallbackInterface?.onImageJokeCallback()
        }
        holder.img_whatsapp_share.setOnClickListener{
//            ClassShareApp(context).shareImg1(holder.img_picture, listDetail.joke_image_url!!, context)
            ClassShareApp(context).shareImg2(holder.img_picture)
        }
        holder.img_share_btn.setOnClickListener{
//            ClassShareApp(context).shareImg1(holder.img_picture, listDetail.joke_image_url!!, context)
            ClassShareApp(context).shareImg2(holder.img_picture, "others")
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


    open inner class ViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener{
        override fun onClick(v: View?) {

        }
        val img_wrapper = v.img_wrapper!!
        val img_picture = v.img_picture!!
        val img_whatsapp_share = v.img_whatsapp_share!!
        val img_share_btn = v.img_share_btn!!
        val img_delete_btn = v.img_delete_btn
    }



    //interface declaration
    interface ImageJokeAdapterCallbackInterface {
        fun onImageJokeCallback()
    }
}