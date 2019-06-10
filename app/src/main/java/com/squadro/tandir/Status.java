package com.squadro.tandir;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }



}
