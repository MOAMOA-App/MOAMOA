package com.example.moamoa.ui.home;

import java.time.format.SignStyle;

public class homelist_data {
    private String mainImage;
    private String title;
    private String cost;
    private String mans;
    private String FID;
    private String UID;
    private String category;
    private String state;
    private String location;
    /*
    public homelist_data(String mainImage, String title, String name, String mans, String FID){
        this.mainImage  = mainImage;
        this.title      = title;
        this.name       = name;
        this.mans       = mans;
        this.FID        = FID;
    }
    */
    public String getImgName(){
        return this.mainImage;
    }

    public String getTitle(){
        return this.title;
    }

    public String getCost(){ return this.cost; }

    public String getMans(){ return this.mans; }

    public String getFID() {return this.FID;}

    public String getUID() {return this.UID;}

    public String getCategory() {return this.category;}

    public String getState(){return this.state;}

    public String getLocation(){return this.location;}

    public void setImgName(String MainImage){this.mainImage = MainImage; }

    public void setTitle(String title){this.title = title; }

    public void setCost(String cost){this.cost = cost; }

    public void setMans(String mans){this.mans = mans; };

    public void setFID(String FID){this.FID = FID; }

    public void setUID(String UID){this.UID = UID; }

    public void setCategory(String category){this.category = category; }

    public void setState(String state){this.state = state; }

    public void setLocation(String location){this.location = location; }

}
