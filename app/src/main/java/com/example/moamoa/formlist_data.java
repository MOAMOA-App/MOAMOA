package com.example.moamoa;

public class formlist_data {
    private int mainImage;
    private String charge;
    private String title;
    private String name;
    private String deadline;

    public formlist_data(int mainImage, String money, String title, String name, String deadline){
        this.mainImage  = mainImage;
        this.charge     = money;
        this.title      = title;
        this.name       = name;
        this.deadline   = deadline;
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

    public String getDeadline(){
        return this.deadline;
    }
}
