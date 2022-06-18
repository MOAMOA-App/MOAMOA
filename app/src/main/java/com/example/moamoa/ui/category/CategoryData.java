package com.example.moamoa.ui.category;

public class CategoryData {

    private String numb;
    private String name;
    public CategoryData(String numb, String name){
        this.numb  = numb;
        this.name  = name;
    }
    public String getName(){
        return this.name;
    }

    public String getNumb(){
        return this.numb;
    }

    public void String(String numb){this.numb = numb; }

    public void setName(String name){this.name = name; }
}
