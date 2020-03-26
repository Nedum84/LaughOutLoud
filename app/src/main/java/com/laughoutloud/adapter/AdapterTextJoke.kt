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
import kotlinx.android.synthetic.main.adapter_item_text.view.*
import kotlinx.android.synthetic.main.alert_dialog_edit_joke_text.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class AdapterTextJoke(val list:MutableList<TextClassBinder>, val context: Context) : RecyclerView.Adapter<AdapterTextJoke.ViewHolder>() {
    private var adapterCallbackInterface: TextJokeAdapterCallbackInterface? = null


    init {
        setHasStableIds(true)
        try {
            adapterCallbackInterface = context as TextJokeAdapterCallbackInterface
        } catch (e: ClassCastException) {
//            throw RuntimeException(context.toString() + "Activity must implement TextJokeAdapterCallbackInterface.", e)
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

        holder.text_body.text = listDetail.joke_text

        holder.text_time.text = ClassDateAndTime().checkDateTimeFirst(listDetail.joke_time!!.toLong())
        holder.text_no_of_like.text = listDetail.joke_no_of_like
        holder.text_no_of_comment.text = listDetail.joke_no_of_comment


        if ((ClassSharedPreferences(context).getUserDetails("user_level")!!.toInt()>1||listDetail.joke_user_id == ClassSharedPreferences(context).getUserDetails("id")))
            holder.text_edit_btn.visibility = View.VISIBLE else holder.text_edit_btn.visibility = View.GONE



        holder.text_like_btn.setOnClickListener{
            if (listDetail.is_already_liked as Boolean){//already liked
                ClassAlertDialog(context).toast("Already liked this...")
            }else{
                sendLikeRequest(listDetail)
            }
        }
        holder.text_comment_btn.setOnClickListener{
            editDialog(listDetail, position)
        }
        holder.text_edit_btn.setOnClickListener{
            adapterCallbackInterface?.onTextJokeCallback()
        }
        //sharing...
        holder.text_whatsapp_share.setOnClickListener {
            ClassShareApp(context).shareText(listDetail.joke_text!!)
        }
        holder.text_share_btn.setOnClickListener {
            ClassShareApp(context).shareText(listDetail.joke_text!!, "others")
        }
    }

    private fun sendLikeRequest(listDetail: TextClassBinder) {
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
                        Toast.makeText(context, "You liked this...", Toast.LENGTH_LONG).show()


                        listDetail.is_already_liked = true

                        notifyDataSetChanged()
                    }else{
                        ClassAlertDialog(context).toast(responseStatus)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            com.android.volley.Response.ErrorListener { _ ->
                pDialog.dismissDialog()
                ClassAlertDialog(context).toast("NETWORK ERROR!, TRY AGAIN")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val params = HashMap<String, String?>()
                params["request_type"] = "like_post"
                params["user_id"] = ClassSharedPreferences(context).getUserDetails("id")
                params["joke_id"] = "${listDetail.joke_id}"
                return params
            }
        }
        //adding request to queue
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
        //volley interactions end
    }


    private fun editDialog(listDetail: TextClassBinder,position: Int) {
        val inflater = LayoutInflater.from(context).inflate(R.layout.alert_dialog_edit_joke_text, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(inflater)
        builder.setCancelable(false)
        val dialog = builder.create()
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()



        inflater.jokeMsgBody.setText(listDetail.joke_text)

        inflater.closeDialog.setOnClickListener {
            dialog.dismiss()
        }
        inflater.text_update_btn.setOnClickListener {
            publishEdited(inflater,listDetail,dialog)
        }
        inflater.text_delete_btn.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Delete this Question?")
                .setPositiveButton("Delete"
                ) { _, _ ->
                    delPost(listDetail,dialog,position)
                }.setNegativeButton("cancel"
                ) { _, _ ->
                }.show()
        }

    }

    private fun publishEdited(inflater: View?, listDetail: TextClassBinder, aDialog: AlertDialog) {
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
                        aDialog.dismiss()
                        ClassAlertDialog(context).toast("Post Edited successfully...")



                        listDetail.joke_text = inflater!!.jokeMsgBody.text.toString()

                        notifyDataSetChanged()
                    }else{
                        ClassAlertDialog(context).toast(responseStatus)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            com.android.volley.Response.ErrorListener { _ ->
                pDialog.dismissDialog()
                ClassAlertDialog(context).toast("Post couldn't be Edited (NETWORK ERROR!)")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val params = HashMap<String, String?>()
                params["request_type"] = "update_edited_post"
                params["joke_text"] = inflater!!.jokeMsgBody.text.toString()
                params["joke_id"] = "${listDetail.joke_id}"
                return params
            }
        }
        //adding request to queue
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
        //volley interactions end
    }

    private fun delPost(listDetail: TextClassBinder, aDialog: AlertDialog, position: Int) {
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
                        aDialog.dismiss()

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
        val text_wrapper = v.text_wrapper!!
        val text_body = v.text_body!!
        val text_time = v.text_time!!
        val text_like_btn = v.text_like_btn!!
        val text_no_of_like = v.text_no_of_like
        val text_whatsapp_share = v.text_whatsapp_share!!
        val text_share_btn = v.text_share_btn!!
        val text_edit_btn = v.text_edit_btn!!
        val text_comment_btn = v.text_comment_btn!!
        val text_no_of_comment = v.text_no_of_comment!!
    }



    //interface declaration
    interface TextJokeAdapterCallbackInterface {
        fun onTextJokeCallback()
    }
}