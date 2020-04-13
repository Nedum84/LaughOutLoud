package com.happinesstonic.ui.fragment


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.happinesstonic.*
import com.happinesstonic.adapter.AdapterTextJoke
import com.happinesstonic.utils.ClassAlertDialog
import com.happinesstonic.utils.ClassSharedPreferences
import com.happinesstonic.utils.TextClassBinder
import com.happinesstonic.viewmodel.ModelHomeActivity
import kotlinx.android.synthetic.main.fragment_joke.*
import kotlinx.android.synthetic.main.inc_network_error_page.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class FragmentTextJoke : Fragment() {
    private lateinit var thisContext: Activity
    lateinit var modelHomeActivity: ModelHomeActivity

    val linearLayoutManager = LinearLayoutManager(activity)
    private var jokeList: MutableList<TextClassBinder>? = mutableListOf()

    lateinit var ADAPTER : AdapterTextJoke
    var start_page_from = 0
    private var JOKE_TYPE = "text"
    var isLoadingDataFromServer = false  //for checking when data fetching is going on


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_joke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        thisContext = activity!!


        ADAPTER = AdapterTextJoke(jokeList!!, thisContext)
        joke_recycler?.layoutManager = linearLayoutManager
        joke_recycler?.itemAnimator = DefaultItemAnimator()
        joke_recycler?.adapter = ADAPTER

        loadJokeFromServer()

//        swipe to refresh data
        swipe_to_refresh?.setOnRefreshListener {
            swipe_to_refresh?.isRefreshing = false
            refreshList()
        }
        tapToRetry.setOnClickListener {
            swipe_to_refresh?.isRefreshing = false
            refreshList()
        }


        joke_recycler?.isNestedScrollingEnabled = false
        fragNestedScrollView?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (!isLoadingDataFromServer){
                    isLoadingDataFromServer = true
                    loadJokeFromServer()
                }
            }
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        modelHomeActivity = activity!!.run{
            ViewModelProvider(this).get(ModelHomeActivity::class.java)
        }

        modelHomeActivity.fragRefreshJokeText.observe(activity!!, Observer {
            it?.getContentIfNotHandled()?.let { joke_type ->
                if(joke_type == "text"){
                    refreshList()
                }
            }
        })

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // retain this fragment when activity is re-initialized
//        retainInstance = true
    }

    private fun refreshList(){
        jokeList!!.clear()
        ADAPTER.addItems(jokeList!!)
        ADAPTER.notifyDataSetChanged()
        loadJokeFromServer()
    }


    private fun loadJokeFromServer(){
        start_page_from = if(jokeList!!.size==0){0}else{jokeList!!.minBy { it.joke_id }!!.joke_id}
//        start_page_from = if(jokeList!!.size==0){0}else{jokeList!!.sortedByDescending { it.joke_id }.last().joke_id!!}
        nwErrorPageWrapper?.visibility = View.GONE
        loadingProgressbar?.visibility = View.VISIBLE
        val stringRequest = object : StringRequest(Method.POST, UrlHolder.URL_GET_TEXT_JOKE,
            Response.Listener { response ->
                isLoadingDataFromServer = false
                loadingProgressbar?.visibility = View.GONE

                try {
                    val obj = JSONObject(response)
                    if (!obj.getBoolean("error")) {
                        val jsonResponse = obj.getJSONArray("joke_arrayszxz")

                        if ((jsonResponse.length()!=0)){
                            val newDataArray = mutableListOf<TextClassBinder>()
                            for (i in 0 until jsonResponse.length()) {
                                val jsonObj = jsonResponse.getJSONObject(i)
                                val nData =
                                    TextClassBinder(
                                        jsonObj.getInt("joke_id"),
                                        jsonObj.getString("joke_cat"),
                                        jsonObj.getString("joke_type"),
                                        jsonObj.getString("joke_text"),
                                        jsonObj.getString("joke_image_url"),
                                        jsonObj.getString("joke_video_url"),
                                        jsonObj.getString("joke_time"),
                                        jsonObj.getString("joke_no_of_like"),
                                        jsonObj.getString("joke_no_of_comment"),
                                        jsonObj.getString("joke_user_id"),
                                        jsonObj.getBoolean("is_already_liked")
                                    )
                                if (nData !in jokeList!!)newDataArray.add(nData)
                            }
                            ADAPTER.addItems(newDataArray)

                        }
//                        else if(!isReloading){
//                            no_data_tag?.visibility = View.VISIBLE
//                        }
                        else{
                            ClassAlertDialog(thisContext).toast("No more data...")
                        }
                    } else {
                        ClassAlertDialog(thisContext).toast("An error occurred, try again...")
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { _er ->
                _er.printStackTrace()
                isLoadingDataFromServer = false
                loadingProgressbar?.visibility = View.GONE
                if(jokeList!!.size == 0)
                    nwErrorPageWrapper?.visibility = View.VISIBLE
                else
                    ClassAlertDialog(thisContext).toast("No internet connection...")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val params = HashMap<String, String?>()
                params["request_type"] = "get_jokes"
                params["joke_type"] = JOKE_TYPE
                params["start_page_from"] = "$start_page_from"
                params["user_id"] = ClassSharedPreferences(
                    thisContext
                ).getUserId()
                return params
            }
        }

        VolleySingleton.instance?.addToRequestQueue(stringRequest)//adding request to queue
    }


}


