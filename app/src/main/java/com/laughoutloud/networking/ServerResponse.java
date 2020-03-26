package com.laughoutloud.networking;

/**
 * Created by delaroystudios on 10/5/2016.
 */

import com.google.gson.annotations.SerializedName;


public class ServerResponse {

    // variable name should be same as in the json response from php
    @SerializedName("success")
    private
    boolean success;
    @SerializedName("message")
    private
    String message;

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }

}