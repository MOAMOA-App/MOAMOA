package com.example.moamoa.ui.home;

public class formlist_data {
    private int mainImage;
    private String charge;
    private String title;
    private String name;
    private String mans;

    public formlist_data(int mainImage, String money, String title, String name, String mans){
        this.mainImage  = mainImage;
        this.charge     = money;
        this.title      = title;
        this.name       = name;
        this.mans   = mans;
    }
    public int getMainImage(){
        return this.mainImage;
    }

    public String getCharge(){
        return this.charge;
    }

    public String getTitle(){
        return this.title;
    }

    public String getName(){
        return this.name;
    }

    public String getMans(){
        return this.mans;
    }
}
