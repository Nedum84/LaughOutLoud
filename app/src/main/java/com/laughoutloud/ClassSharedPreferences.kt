package com.laughoutloud

import android.content.Context
import com.google.gson.Gson

class  ClassSharedPreferences(val context: Context?){

    private val PREFERENCE_NAME = "subjects_interactions_preference"
    private val PREFERENCE_ALL_SUBJECTS = "all_subjects"
    private val PREFERENCE_CURRENT_SUB_ID = "current_subject_id"
    private val PREFERENCE_CURRENT_SUB_NAME = "current_subject_name"
    private val PREFERENCE_CURRENT_TOPIC_ID = "current_topic_id"
    private val PREFERENCE_CURRENT_TOPIC_NAME = "current_topic_name"
    private val PREFERENCE_CURRENT_ANSWER_ID = "current_answer_id"
    private val PREFERENCE_VIEW_CURRENT_QUESTION_DETAILS = "view_current_q_details"
    private val PREFERENCE_CURRENT_VIDEO_DETAILS = "current_video_details"
    private val PREFERENCE_HOME_FRAG_REDIRECT = "home_frag_redirect"
    private val PREFERENCE_CURRENT_HOME_ACTIVITY_TITLE = "home_activity_title"
    private val PREFERENCE_SCHOOL_ID= "school_id"
    private val PREFERENCE_POSTER_ID= "poster_id"
    private val PREFERENCE_POSTER_NAME= "poster_name"
    private val PREFERENCE_Q_IMG_URL= "q_img_url"
    private val PREFERENCE_EXAM_TYPE= "current_exam_type"
    private val PREFERENCE_ANSWERED_STATUS= "current_answered_status"
    private val PREFERENCE_SCAN_CHECK= "scan_check_from_crop_activity"
    private val PREFERENCE_CURRENT_ACTIVITY_FROM_BASE_ACT= "current_activity_from_base_activity"
    private val PREFERENCE_CURRENT_DRAFT_ID= "current_draft_id"
    private val PREFERENCE_CURRENT_EXAM_TYPE_ID= "current_exam_type_id"
    private val PREFERENCE_TIME_FOR_NEXT_PACKAGE_DOWNLOADS= "time_for_next_package_download"
    private val PREFERENCE_SUB_EXPIRY_DATE= "subscription_expiry_date"
    private val PREFERENCE_IS_SUB_ACTIVE= "is_subscription_active"
    private val PREFERENCE_ROOM_COMMENTS_DETAILS= "room_comments_details"
    private val PREFERENCE_EXAM_GAME_LEADER_BOARD_LIST= "exam_game_lists"

    //exam prefs
    private val PREFERENCE_CURRENT_EXAM_SUB_ID= "current_exam_sub_id"
    private val PREFERENCE_CURRENT_EXAM_YEAR= "current_exam_exam_year"
    private val PREFERENCE_CURRENT_EXAM_TIME= "current_exam_exam_time"
    private val PREFERENCE_CURRENT_EXAM_SCHOOL= "current_exam_school"
    private val PREFERENCE_CURRENT_EXAM_APT_CAT= "current_exam_exam_apt_cat"
    private val PREFERENCE_CURRENT_EXAM_APT_CAT_TOPIC_ID= "current_exam_apt_cat_topic_id"
    private val PREFERENCE_EXAM_TIME_SPENT= "current_exam_time_spent"
    private val PREFERENCE_PAUSE_TO_CONTINUE_LATER= "current_pause_to_continue_later"
    private val PREFERENCE_CURRENT_EXAM_DETAILS = "current_exam_qs_details"
    private val PREFERENCE_CURRENT_EXAM_SCORE_ID = "current_exam_score_id"
    private val PREFERENCE_CURRENT_CHAT_RECEIVER_ID = "current_chat_receiver_id"
    private val PREFERENCE_CURRENT_CHAT_RECEIVER_NAME = "current_chat_receiver_name"
    private val PREFERENCE_CURRENT_CHAT_RECEIVER_PHOTO = "current_chat_receiver_photo"
    private val PREFERENCE_CURRENT_CHAT_EXAM_TYPE_ID = "current_chat_exam_type_id"
    private val PREFERENCE_CONFIRM_PAYMENT_USER_DETAILS = "confirm_payment_user_details"
    private val PREFERENCE_APP_UPDATE_DETAILS_JSON = "app_update_details_json"



    //user login details
    private val PREFERENCE_LOGGED_IN_DETAILS = "loggedInPreferenceName"
    private val PREFERENCE_USER_ID = "login_user_id"
    private val PREFERENCE_USER_NAME = "login_user_name"
    private val PREFERENCE_USER_USERNAME = "login_user_username"
    private val PREFERENCE_USER_EMAIL = "login_user_email"
    private val PREFERENCE_USER_MOBILE_NO = "login_user_mobile_no"
    private val PREFERENCE_USER_MEMBER_ID = "login_user_member_id"
    private val PREFERENCE_USER_PROFILE_PICTURE = "login_user_profile_picture"
    private val PREFERENCE_USER_POSTS_AND_ANS = "user_posts_and_ans"
    private val PREFERENCE_USER_LEVEL = "user_level"

    private val loggedInPreference = context?.getSharedPreferences(PREFERENCE_LOGGED_IN_DETAILS,Context.MODE_PRIVATE)!!
    private val preference = context?.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)!!

    //set current_activity_from_base_activity
    fun setCurrentActFromBaseAct(subjects:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_ACTIVITY_FROM_BASE_ACT,subjects)
        editor.apply()
    }
    //get current_activity_from_base_activity
    fun getCurrentActFromBaseAct():String?{
        return  preference.getString(PREFERENCE_CURRENT_ACTIVITY_FROM_BASE_ACT,"question_view")
    }
    //set subjects
    fun setSubjects(subjects:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_ALL_SUBJECTS,subjects)
        editor.apply()
    }
    //get current sub id
    fun getSubjects():String?{
        return  preference.getString(PREFERENCE_ALL_SUBJECTS,"1")
    }
    //set subject name
    fun setCurrentSubjectName(subject_name:String?){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_SUB_NAME, subject_name)
        editor.apply()
    }
    //get current sub name
    fun getCurrentSubjectName():String?{
        return  preference.getString(PREFERENCE_CURRENT_SUB_NAME,"1")
    }
    //set current sub id
    fun setCurrentSubjectId(subject_id:String?){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_SUB_ID,subject_id)
        editor.apply()
    }
    //get current sub id
    fun getCurrentSubjectId() : String?{
        return  preference.getString(PREFERENCE_CURRENT_SUB_ID,"-1")
    }
    //    get current subject topic
    fun getCurrentTopicName():String?{
        return  preference.getString(PREFERENCE_CURRENT_TOPIC_NAME,"0")
    }
    //set current subject_name
    fun setCurrentTopicName(topic_name:String?){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_TOPIC_NAME,topic_name)
        editor.apply()
    }
    //set current subject_topic
    fun setCurrentTopicId(topic_id:String?){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_TOPIC_ID,topic_id)
        editor.apply()
    }
    //    get current subject name
    fun getCurrentTopicId():String?{
        return  preference.getString(PREFERENCE_CURRENT_TOPIC_ID,"0")
    }
    //set current subject_topic
    fun setCurrentQuestionId(question_id:String?){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_ANSWER_ID,question_id)
        editor.apply()
    }
    //    get current subject topic
    fun getCurrentQuestionId():String?{
        return  preference.getString(PREFERENCE_CURRENT_ANSWER_ID,"0")
    }
    //set current questions to view details
    fun setCurrentViewQDetails(question_id:String?){
        val editor = preference.edit()
        editor.putString(PREFERENCE_VIEW_CURRENT_QUESTION_DETAILS,question_id)
        editor.apply()
    }
    //    geet current questions to view details
    fun getCurrentViewQDetails():String?{
        return  preference.getString(PREFERENCE_VIEW_CURRENT_QUESTION_DETAILS,"0")
    }
    //set current Video Details
    fun setCurrentVideoDetails(video_details:String?){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_VIDEO_DETAILS,video_details)
        editor.apply()
    }
    //    get current Video Details
    fun getCurrentVideoDetails():String?{
        return  preference.getString(PREFERENCE_CURRENT_VIDEO_DETAILS,"")
    }

    //set redirection path for home activity
    fun setHomeFragRedirect(redirectTo:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_HOME_FRAG_REDIRECT,redirectTo)
        editor.apply()
    }
    //    get redirection path for home activity
    fun getHomeFragRedirect():String?{
        return  preference.getString(PREFERENCE_HOME_FRAG_REDIRECT,"home")
    }

    //    display home activity title
    fun setCurrentHomeActivityTitle(screenTitle: String?) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_HOME_ACTIVITY_TITLE,screenTitle)
        editor.apply()
    }
    //    get current subject topic
    fun getCurrentHomeActivityTitle():String?{
        return  preference.getString(PREFERENCE_CURRENT_HOME_ACTIVITY_TITLE, "Tutorial" )
    }
    //set SchoolId
    fun setSchoolId(q_code:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_SCHOOL_ID,q_code)
        editor.apply()
    }
    //get SchoolId
    fun getSchoolId():Int?{
        return  preference.getInt(PREFERENCE_SCHOOL_ID,-1)//1->subject, 2->topic, 3->posterQuestions(the user that posted the questions), 4->myQuestions
    }
    //set poster id
    fun setPosterId(q_code:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_POSTER_ID,q_code)
        editor.apply()
    }
    //get poster id
    fun getPosterId():String?{
        return  preference.getString(PREFERENCE_POSTER_ID,"-1")
    }

    //get poster name
    fun getPosterName():String?{
        return  preference.getString(PREFERENCE_POSTER_NAME,"JAMB & POST UTME Ask Me")
    }
    //set poster name
    fun setPosterName(name:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_POSTER_NAME,name)
        editor.apply()
    }

    //get Exam Type
    fun getExamType():String?{
        return  preference.getString(PREFERENCE_EXAM_TYPE,"-1")
    }
    //set Exam Type
    fun setExamType(name:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_EXAM_TYPE,name)
        editor.apply()
    }

    //get AnsweredStatus
    fun getAnsweredStatus():String?{
        return  preference.getString(PREFERENCE_ANSWERED_STATUS,"-1")
    }
    //set AnsweredStatus
    fun setAnsweredStatus(name:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_ANSWERED_STATUS,name)
        editor.apply()
    }

    //set questions img for cropping
    fun setQImgUrl(url:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_Q_IMG_URL,url)
        editor.apply()
    }
    //get questions img for cropping
    fun getQImgUrl():String{
        return  preference.getString(PREFERENCE_Q_IMG_URL,"")!!
    }

    //scan text?
    fun setScanCheck(url:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_SCAN_CHECK,url)
        editor.apply()
    }
    //get questions img for cropping
    fun getScanCheck():String{
        return  preference.getString(PREFERENCE_SCAN_CHECK,"")!!
    }
    //set draft id?
    fun setCurrentDraftId(url:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_DRAFT_ID,url)
        editor.apply()
    }
    //get draft id
    fun getCurrentDraftId():String{
        return  preference.getString(PREFERENCE_CURRENT_DRAFT_ID,"")!!
    }
    //set exam type
    fun setCurrentExamTypeId(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_EXAM_TYPE_ID,data)
        editor.apply()
    }
    //get exam type
    fun getCurrentExamTypeId():String{
        return  preference.getString(PREFERENCE_CURRENT_EXAM_TYPE_ID,"")!!
    }
    //set chat receiver id
    fun setCurrentChatReceiverId(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_CHAT_RECEIVER_ID,data)
        editor.apply()
    }
    //get chat receiver id
    fun getCurrentChatReceiverId():String{
        return  preference.getString(PREFERENCE_CURRENT_CHAT_RECEIVER_ID,"")!!
    }
    //set chat receiver NAME
    fun setCurrentChatReceiverName(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_CHAT_RECEIVER_NAME,data)
        editor.apply()
    }
    //get chat receiver NAME
    fun getCurrentChatReceiverName():String{
        return  preference.getString(PREFERENCE_CURRENT_CHAT_RECEIVER_NAME,"")!!
    }
    //set chat receiver PHOTO
    fun setCurrentChatReceiverPhoto(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_CHAT_RECEIVER_PHOTO,data)
        editor.apply()
    }
    //get chat receiver PHOTO
    fun getCurrentChatReceiverPhoto():String{
        return  preference.getString(PREFERENCE_CURRENT_CHAT_RECEIVER_PHOTO,"")!!
    }
    //set chat exam type id
    fun setCurrentChatExamTypeId(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_CHAT_EXAM_TYPE_ID,data)
        editor.apply()
    }
    //get chat exam type id
    fun getCurrentChatExamTypeId():String{
        return  preference.getString(PREFERENCE_CURRENT_CHAT_EXAM_TYPE_ID,"")!!
    }
    //set TIME FOR NEXT DEFAULT PACKAGE DOWNLOADS
    fun setTimeForNextPackageDownloads(){
        val sysSecs = (System.currentTimeMillis()/1000)+ 5*86400//+2 day
        val editor = preference.edit()
        editor.putLong(PREFERENCE_TIME_FOR_NEXT_PACKAGE_DOWNLOADS, sysSecs)
        editor.apply()
    }
    //Get TIME FOR NEXT DEFAULT PACKAGE DOWNLOADS
    fun getTimeForNextPackageDownloads():Long{
        return  preference.getLong(PREFERENCE_TIME_FOR_NEXT_PACKAGE_DOWNLOADS,12345677)
    }

    //SUBSCRIPTIONSZAS...
    //set sub expiry date
    fun setSubExpiryDate(data:Long){
        val editor = preference.edit()
        editor.putLong(PREFERENCE_SUB_EXPIRY_DATE,data)
        editor.apply()
    }
    //get sub expiry date
    fun getSubExpiryDate():Long{
        return  preference.getLong(PREFERENCE_SUB_EXPIRY_DATE,12345677)
    }
    //set sub active
    fun setIsSubActive(choice:Boolean){
        val editor = preference.edit()
        editor.putBoolean(PREFERENCE_IS_SUB_ACTIVE,choice)
        editor.apply()
    }

    //get  sub active
    fun getIsSubActive():Boolean{
        return  preference.getBoolean(PREFERENCE_IS_SUB_ACTIVE,false)
    }
    //confirm payment user details array
    fun setConfirmPaymentUserDetails(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CONFIRM_PAYMENT_USER_DETAILS,data)
        editor.apply()
    }
    fun getConfirmPaymentUserDetails():String{
        return  preference.getString(PREFERENCE_CONFIRM_PAYMENT_USER_DETAILS,"")!!
    }

    //ROOM COMMENTS DETAILS
    fun setCurrentRoomCommentDetails(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_ROOM_COMMENTS_DETAILS,data)
        editor.apply()
    }
    fun getCurrentRoomCommentDetails():String{
        return  preference.getString(PREFERENCE_ROOM_COMMENTS_DETAILS,"")!!
    }

    //Exam leader board JSON DETAILS
    fun setExamLeaderBoardLists(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_EXAM_GAME_LEADER_BOARD_LIST, data)
        editor.apply()
    }
    fun getExamLeaderBoardLists():String{
        return  preference.getString(PREFERENCE_EXAM_GAME_LEADER_BOARD_LIST,"")!!
    }








    //EXAM STUFFS
    fun setCurrentExamSubId(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_EXAM_SUB_ID,data)
        editor.apply()
    }
    fun getCurrentExamSubId():String{
        return  preference.getString(PREFERENCE_CURRENT_EXAM_SUB_ID,"")!!
    }

    fun setCurrentExamYear(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_EXAM_YEAR,data)
        editor.apply()
    }
    fun getCurrentExamYear():String{
        return  preference.getString(PREFERENCE_CURRENT_EXAM_YEAR,"123456")!!//default of random
    }

    fun setCurrentExamTime(data:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_CURRENT_EXAM_TIME,data)
        editor.apply()
    }
    fun getCurrentExamTime():Int{
        return  preference.getInt(PREFERENCE_CURRENT_EXAM_TIME,40)
    }

    fun setCurrentExamSchool(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_EXAM_SCHOOL,data)
        editor.apply()
    }
    fun getCurrentExamSchool():String{
        return  preference.getString(PREFERENCE_CURRENT_EXAM_SCHOOL,"40")!!
    }

    fun setCurrentExamAptCat(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_EXAM_APT_CAT,data)
        editor.apply()
    }
    fun getCurrentExamAptCat():String{
        return  preference.getString(PREFERENCE_CURRENT_EXAM_APT_CAT,"40")!!
    }

    fun setCurrentExamAptCatTopicId(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_EXAM_APT_CAT_TOPIC_ID,data)
        editor.apply()
    }
    fun getCurrentExamAptCatTopicId():String{
        return  preference.getString(PREFERENCE_CURRENT_EXAM_APT_CAT_TOPIC_ID,"40")!!
    }


    fun setExamQTimeSpent(data:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_EXAM_TIME_SPENT,data)
        editor.apply()
    }
    fun getExamQTimeSpent():Int{
        return  preference.getInt(PREFERENCE_EXAM_TIME_SPENT,10)//10 MINS DEFAULT
    }
    //pause to continue exam later...
    fun setPauseToContinueLater(data:Boolean){
        val editor = preference.edit()
        editor.putBoolean(PREFERENCE_PAUSE_TO_CONTINUE_LATER,data)
        editor.apply()
    }
    fun getPauseToContinueLater():Boolean{
        return  preference.getBoolean(PREFERENCE_PAUSE_TO_CONTINUE_LATER,false)
    }

    //current exam details
    fun setCurrentExamQsDetails(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_EXAM_DETAILS,data)
        editor.apply()
    }
    fun getCurrentExamQsDetails():String{
        return  preference.getString(PREFERENCE_CURRENT_EXAM_DETAILS,"")!!
    }

    //current exam score for results and explanation table
    fun setCurrentExamScoreId(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_CURRENT_EXAM_SCORE_ID,data)
        editor.apply()
    }
    fun getCurrentExamScoreId():String{
        return  preference.getString(PREFERENCE_CURRENT_EXAM_SCORE_ID,"1")!!
    }



    fun reset(){
        setCurrentSubjectId("-1")
        setCurrentSubjectName("")
        setCurrentTopicId("-1")
        setCurrentTopicName("")
        setPosterId("-1")
        setPosterName("")
        setAnsweredStatus("-1")
        setExamType("-1")
        setSchoolId(-1)
    }
    fun clear(){
        val userEditor = preference.edit()
        userEditor.clear()
        userEditor.apply()
    }










    //user login  details
    //store login details
    fun saveLoginDetails(login_user_id:String?,
                         login_user_name:String?,
                         login_user_username:String?,
                         login_user_email:String?,
                         login_user_mobile_no:String?,
                         login_user_member_id:String?,
                         login_user_profile_picture:String?,
                         login_user_posts_and_ans:String?,
                         login_user_level:String?):Boolean{
        val userEditor = loggedInPreference.edit()
        userEditor.putString(PREFERENCE_USER_ID,login_user_id)
        userEditor.putString(PREFERENCE_USER_NAME,login_user_name)
        userEditor.putString(PREFERENCE_USER_USERNAME,login_user_username)
        userEditor.putString(PREFERENCE_USER_EMAIL,login_user_email)
        userEditor.putString(PREFERENCE_USER_MOBILE_NO,login_user_mobile_no)
        userEditor.putString(PREFERENCE_USER_MEMBER_ID,login_user_member_id)
        userEditor.putString(PREFERENCE_USER_PROFILE_PICTURE,login_user_profile_picture)
        userEditor.putString(PREFERENCE_USER_POSTS_AND_ANS,login_user_posts_and_ans)
        userEditor.putString(PREFERENCE_USER_LEVEL,login_user_level)
        userEditor.apply()

        return true
    }
    //    get current subject topic
    fun getUserDetails(id:String):String?{
        return when (id) {
            "id" -> loggedInPreference.getString(PREFERENCE_USER_ID,"-1")//-1
            "name" -> loggedInPreference.getString(PREFERENCE_USER_NAME, context?.getString(R.string.app_name))
            "username" -> loggedInPreference.getString(PREFERENCE_USER_USERNAME, context?.getString(R.string.app_name)!!.replace(" ","").toLowerCase())
            "email" -> loggedInPreference.getString(PREFERENCE_USER_EMAIL,null)
            "mobile_no" -> loggedInPreference.getString(PREFERENCE_USER_MOBILE_NO,"")
            "member_id" -> loggedInPreference.getString(PREFERENCE_USER_MEMBER_ID,"")
            "profile_picture" -> loggedInPreference.getString(PREFERENCE_USER_PROFILE_PICTURE,"")
            "posts_and_ans" -> loggedInPreference.getString(PREFERENCE_USER_POSTS_AND_ANS,"0 / 0")
            "user_level" -> loggedInPreference.getString(PREFERENCE_USER_LEVEL,"1")//1->app_logo, 2->teacher, 3->admin, 4-> Super Admin, 5->support
            else -> loggedInPreference.getString(PREFERENCE_USER_NAME,"")
        }
    }
    fun isLoggedIn():Boolean{
        return getUserDetails("email")!=null
    }
    fun logoutUser() : Boolean{
        val userEditor = loggedInPreference.edit()
        userEditor.clear()
        userEditor.apply()

        setIsSubActive(false)//disable subscription...

        return true
    }
}