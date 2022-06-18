package com.example.moamoa.ui.account;

import java.util.ArrayList;
import java.util.Arrays;

public class Random_nick {
    ArrayList<String> nickarray = new ArrayList<String>(Arrays.asList("무지개","분홍","오렌지","개나리","연두","해변의","퍼렁","보라","갈색","하얀"));
    ArrayList<String> chararray = new ArrayList<String>(Arrays.asList("웨옹","곰돌이","귀긴곰","꽥","고양이","고양이","곰","토끼","오리"));
    private String nickname;
    private String image;
    public void Random_nick(){
        int randomNum1 = (int) (Math.random() * 10);
        int randomNum2 = (int) (Math.random() * 8)%4;
        this.nickname = nickarray.get(randomNum1)+chararray.get(randomNum2);
        this.image = randomNum1+"_"+randomNum2;
    }
    public String getNickname() {
        return nickname;
    }
    public String getImage(){
        return image;
    }
    public void setNickname(){
        int randomNum1 = (int) (Math.random() * 10);
        int randomNum2 = (int) (Math.random() * 8)%4;
        this.nickname = nickarray.get(randomNum1)+chararray.get(randomNum2);
        this.image = randomNum2+"_"+randomNum1;
    }
}
