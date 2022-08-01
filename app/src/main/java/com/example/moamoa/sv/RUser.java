package com.example.moamoa.sv;

import com.google.gson.annotations.SerializedName;

import java.sql.Array;

public class RUser {
    @SerializedName("uid")
    private String userid;

    @SerializedName("email")
    private String email;

    @SerializedName("category")
    private Array category;

    public RUser() {
    }

    public java.lang.String getUserid() {
        return userid;
    }

    public String getEmail() {
        return email;
    }

    public Array getCategory() {
        return category;
    }
}
