package com.example.moamoa.ui.home;

public class homelist_data {
    private String mainImage;
    private String title;
    private String nick;
    private String mans;
    private String FID;
    private String UID;
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

    public String getNick(){ return this.nick; }

    public String getMans(){ return this.mans; }

    public String getFID() {return this.FID;}

    public String getUID() {return this.UID;}

    public void setImgName(String MainImage){this.mainImage = MainImage; }

    public void setTitle(String title){this.title = title; }

    public void setNick(String nick){this.nick = nick; }

    public void setMans(String mans){this.mans = mans; }

    public void setFID(String FID){this.FID = FID; }

    public void setUID(String UID){this.UID = UID; }
}
