package com.example.moamoa.ui.home;

public class homelist_data {
    private String mainImage;
    private String title;
    private String name;
    private String mans;
    private String FID;
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

    public String getName(){ return this.name; }

    public String getMans(){ return this.mans; }

    public String getFID() {return this.FID;}

    public void setImgName(String MainImage){this.mainImage = MainImage; }

    public void setTitle(String title){this.title = title; }

    public void setName(String name){this.name = name; }

    public void setMans(String mans){this.mans = mans; }

    public void setFID(String FID){this.FID = FID; }
}
