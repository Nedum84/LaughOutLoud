package com.happinesstonic.utils

data class TextClassBinder(val joke_id: Int, val joke_cat: String?, val joke_type: String?, var joke_text: String?, val joke_image_url: String?
                           , val joke_video_url: String? , val joke_time: String?, var joke_no_of_like: String?, var joke_no_of_comment: String?
                           , val joke_user_id: String?, var is_already_liked: Boolean?, var share_to:String= "whatsapp")
//data class ImageClassBinder(val joke_id: String?, val joke_cat: String?, val joke_image_url: String?, val joke_time: String?, val joke_user_id: String?)
//data class VideoClassBinder(val joke_id: String?, val joke_cat: String?, val joke_video_url: String?, val joke_image_url: String?, val joke_time: String?
//                            , val joke_user_id: String?)
