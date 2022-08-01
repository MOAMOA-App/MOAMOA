package com.example.moamoa.sv;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RCategory {
    @SerializedName("cid")
    private int cid;

    @SerializedName("cname")
    private String cname;

    @SerializedName("category")
    private List category;

    public int getCid () {
        return cid;
    }

    public String getCname() {
        return cname;
    }

    public List getCategory(){
        return category;
    }
}
