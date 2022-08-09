package com.example.moamoa.sv;

import com.google.gson.annotations.SerializedName;

public class RUser {
    @SerializedName("uid")
    private String userid;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("profile")
    private String profile;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("now")
    private String now;

    public String getNow() { return now; }

    public String getUserid(){
        return userid;
    }

    public String getEmail(){
        return email;
    }

    public String getNickname(){
        return nickname;
    }

    public String getProfile() {
        return profile;
    }
}
