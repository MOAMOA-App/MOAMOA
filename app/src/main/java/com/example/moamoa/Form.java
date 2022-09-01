package com.example.moamoa;

import android.util.Log;

public class Form  implements Comparable<Form> {
   public String FID; //폼이름
   public String subject; //제목
   public  String text; //내용
    public String image; //이미지
    public  String address; //주소
    public int category_text; //카테고리
    public String cost; //가격
    public String max_count; //마감인원
    public int deadline; //마감기간
    public String today; //작성일
    public String UID_dash; //UID
    public int heart_num; //찜갯수
    public int parti_num; //참여수
    public int state; //상태
    public int count; //조회수

    public int s_case; //
    public String express; //배송종류



    public Form(){ };
    public Form( String FID, String UID_dash,String image, String subject, String text, String address,int category_text, String cost, String max_count,int deadline, String today,int count,String express){
        this.FID = FID;

        this.UID_dash=UID_dash;
        this.subject = subject;
        this.image=image;
        this.text = text;
        this.address = address;
        this.category_text = category_text;
        this.cost = cost;
        this.max_count = max_count;
        this.deadline = deadline;
        this.today = today;
        this.count = count;
        this.express = express;
    }
    @Override
    public int compareTo(Form o) {
        Log.d("MainActivity", "ChildEventListener -  : " + s_case);

            if (s_case==0 && this.count > o.count) {
                return -1;
            } else if (s_case==0 && this.count == o.count) {
                return 0;
            } else if (s_case==0 && this.count < o.count){
                return 1;
            } else if (s_case==1 && this.deadline > o.deadline) {
                return -1;
            } else if (s_case==1 && this.deadline == o.deadline) {
                return 0;
            } else if (s_case==1 && this.deadline < o.deadline) {
                return 1;
            } else if (s_case==2 && this.count < o.count){
                return -1;
            } else if (s_case==2 && this.count == o.count) {
                return 0;
            } else{
                return 1;
            }


    }
 //   public void setUID(String FID){
  //      this.FID = FID;
 //   }

    /*
    public String getFID() {
        return FID;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public String getUID_dash() {
        return UID_dash;
    }

    public void setUID_dash(String UID_dash) {
        this.UID_dash = UID_dash;
    }

     */

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
    public String getCost(){ return cost; }
    public void setCost(String cost) {this.cost = cost;
    }
    public int getCategory_text(){ return category_text; }
    public void setCategory_text(int category_text) {this.category_text = category_text;
    }
    public String getMax_count(){ return max_count; }
    public void setMax_count(String max_count) {this.max_count = max_count;
    }
    public int getDeadline(){ return deadline; }
    public void setDeadline(int deadline) {this.deadline = deadline;
    }
    public String getToday(){ return today; }
    public void setToday(String today) {this.today = today;
    }
    public int getheart_num(){ return heart_num; }
    public void setheart_num(int heart_num) {this.heart_num = heart_num;
    }
    public int getparti_num(){ return parti_num; }
    public void setparti_num(int parti_num) {this.parti_num = parti_num;
    }
    public int getstate(){ return state; }
    public void setstate(int state) {this.state = state;
    }
    public int getCount(){ return count; }
    public void setCount(int count) {this.count = count;
    }
    public String getExpress(){ return express; }
    public void setExpress(String express) {this.express = express;
    }



}
