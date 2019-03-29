package com.example.annoncepei.Networking;

import com.google.gson.annotations.SerializedName;


public class ServerResponse {

    // variable name should be same as in the json response from php
    @SerializedName("filePath")
    String filePath;

    public String getFilePath() {
        return filePath;
    }

}
