package com.example.moamoa.sv;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RCategory {
    @SerializedName("cid")
    private static int cid;

    @SerializedName("cname")
    private static String cname;

    @SerializedName("category")
    private static List category;

    public static int getCid() {
        return cid;
    }

    public String getCname() {
        return cname;
    }

    public static List getCategory(){
        return category;
    }

}
