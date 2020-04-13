package com.happinesstonic.utils

import android.content.Context
import com.google.gson.Gson

class  ClassSharedPreferences(val context: Context?){

    private val PREFERENCE_NAME = "subjects_interactions_preference"
    private val PREFERENCE_CURRENT_DATA_BINDER = "current_data_binder"
    private val PREFERENCE_CURRENT_LIST_BINDER = "current_list_binder"
    private val PREFERENCE_CUR_IMG_UPLOAD_URL= "cur_img_url"


    //user login details
    private val PREFERENCE_USER_ID = "login_user_id"
    private val PREFERENCE_IS_USER_ADMIN = "login_is_user_admin"

    private val preference = context?.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)!!

    //current data list binder
    fun setCurrentDataBinder(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_DATA_BINDER,data)
        editor.apply()
    }
    fun getCurrentDataBinder():String?{
        return  preference.getString(PREFERENCE_CURRENT_DATA_BINDER, Gson().toJson(mutableListOf<TextClassBinder>()))
    }
    //current data list binder
    fun setCurrentListBinder(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_LIST_BINDER,data)
        editor.apply()
    }
    fun getCurrentListBinder():String?{
        return  preference.getString(PREFERENCE_CURRENT_LIST_BINDER, Gson().toJson(mutableListOf<TextClassBinder>()))
    }

    //set questions img for cropping
    fun setQImgUrl(url:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CUR_IMG_UPLOAD_URL,url)
        editor.apply()
    }
    fun getQImgUrl():String{
        return  preference.getString(PREFERENCE_CUR_IMG_UPLOAD_URL,"")!!
    }


    //user login  details
    fun setUserId(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_USER_ID,data)
        editor.apply()
    }
    fun getUserId():String?{
        return  preference.getString(PREFERENCE_USER_ID,null)
    }
    //is user admin
    fun setIsUserAdmin(data:Boolean){
        val editor = preference.edit()
        editor.putBoolean(PREFERENCE_IS_USER_ADMIN,data)
        editor.apply()
    }
    fun isUserAdmin():Boolean{
        return  preference.getBoolean(PREFERENCE_IS_USER_ADMIN, false)
    }

}