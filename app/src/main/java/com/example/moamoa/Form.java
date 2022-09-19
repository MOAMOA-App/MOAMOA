package com.example.moamoa;

import android.util.Log;

public class Form  {
    public String FID;          //폼이름
    public String subject;      //제목
    public String text;         //내용
    public String image;        //이미지
    public int photo_num;       //이미지 갯수
    public String address;      //주소
    public String addr_co;      //경위도
    public String add_detail;      //주소
    public int category_text;   //카테고리
    public int cost;            //가격
    public int max_count;    //마감인원
    public int deadline;        //마감기간
    public String today;        //작성일
    public String UID_dash;     //UID
    public int heart_num;       //찜갯수
    public int parti_num;       //참여수
    public int state;           //상태
    public int count;           //조회수
    public String point;        //위도,경도
    public int s_case;          //지역 상태
    public String express;      //배송종류
    public int active;          //활성화



    public Form(){ };
    public Form( String FID, String UID_dash,String image,int photo_num, String subject, String text,
                 String address,String addr_co,String add_detail,int category_text, int cost, int max_count,int deadline,
                 String today,int count,String express, String point, int active){
        this.FID = FID;

        this.UID_dash   = UID_dash;
        this.subject    = subject;
        this.image      = image;
        this.photo_num  = photo_num;
        this.text       = text;
        this.address    = address;
        this.addr_co = addr_co;
        this.add_detail    = add_detail;
        this.category_text = category_text;
        this.cost       = cost;
        this.max_count  = max_count;
        this.deadline   = deadline;
        this.today      = today;
        this.count      = count;
        this.express    = express;
        this.point      = point;
        this.active     = active;

    }


    public String getSubject(){
        return subject;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
    }
    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public int getCost(){ return cost; }
    public void setCost(int cost) {this.cost = cost;}
    public int getCategory_text(){ return category_text; }
    public void setCategory_text(int category_text) {this.category_text = category_text;}
    public int getMax_count(){ return max_count; }
    public void setMax_count(int max_count) {this.max_count = max_count;}
    public int getDeadline(){ return deadline; }
    public void setDeadline(int deadline) {this.deadline = deadline;}
    public String getToday(){ return today; }
    public void setToday(String today) {this.today = today;}
    public int getheart_num(){ return heart_num; }
    public void setheart_num(int heart_num) {this.heart_num = heart_num;}
    public int getparti_num(){ return parti_num; }
    public void setparti_num(int parti_num) {this.parti_num = parti_num;}
    public int getstate(){ return state; }
    public void setstate(int state) {this.state = state;}
    public int getCount(){ return count; }
    public void setCount(int count) {this.count = count;}
    public String getExpress(){ return express; }
    public void setExpress(String express) {this.express = express;}
    public String getPoint(){ return point; }
    public void setPoint(String point) {this.point = point;}
    public int getPhoto_num(){ return photo_num; }
    public void setPhoto_num(int photo_num) {this.photo_num = photo_num;}
    public int getActive(){ return active; }
    public void setActive(int active){this.active=active;}


}
