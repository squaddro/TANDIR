package com.squadro.tandir;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public Status(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String toString(){
        return "Status Code : "+status+ " Message : "+message;
    }


}
