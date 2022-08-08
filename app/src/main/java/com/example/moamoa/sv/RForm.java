package com.example.moamoa.sv;

import com.google.gson.annotations.SerializedName;

public class RForm {
    @SerializedName("fid")
    private String fid;

    @SerializedName("fuid")
    private String fuid;

    @SerializedName("fcategory")
    private int fcategory;

    @SerializedName("subject")
    private String subject;

    @SerializedName("text")
    private String text;

    @SerializedName("maxnum")
    private int maxnum;

    @SerializedName("nownum")
    private int nownum;

    @SerializedName("startdate")
    private String startdate;

    @SerializedName("deadline")
    private String deadline;

    @SerializedName("location")
    private String location;

    @SerializedName("views")
    private int views;

    @SerializedName("heartnum")
    private int heartnum;


    public String getFid(){
        return fid;
    }

    public String getFuid() {
        return fuid;
    }

    public int getFcategory() {
        return fcategory;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public int getMaxnum() {
        return maxnum;
    }

    public int getNownum() {
        return nownum;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getLocation() {
        return location;
    }

    public int getHeartnum() {
        return heartnum;
    }

    public int getViews() {
        return views;
    }

}
