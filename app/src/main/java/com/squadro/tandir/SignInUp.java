package com.squadro.tandir;

import com.google.gson.annotations.SerializedName;

public class SignInUp {

    @SerializedName("user_name")
    private String user_name;
    @SerializedName("password")
    private String password;

    public String getUser_name() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }

}
