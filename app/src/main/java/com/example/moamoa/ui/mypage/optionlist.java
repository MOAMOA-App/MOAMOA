package com.example.moamoa.ui.mypage;

import android.graphics.drawable.Drawable;

public class optionlist {
    private Drawable image ;
    private String text ;

    public void setImage(Drawable image) {
        this.image = image ;
    }
    public void setText(String text) {
        this.text = text ;
    }

    public Drawable getImage() {
        return this.image ;
    }
    public String getText() {
        return this.text ;
    }

}