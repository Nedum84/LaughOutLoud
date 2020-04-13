package com.happinesstonic


import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Created by Belal on 5/16/2017.
 */

abstract class VolleySingleton : MultiDexApplication(){
//abstract class VolleySingleton : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

//    for drawable error in magical exoplayer
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }



    companion object {
        private val TAG = VolleySingleton::class.java.simpleName
        @get:Synchronized var instance: VolleySingleton? = null
            private set
    }
}