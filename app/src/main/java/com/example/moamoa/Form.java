package com.example.moamoa;

public class Form {
   public String FID;
   public String subject;
   public  String text;
    public String image;
    public  String address;
    public String category_text;
    public String cost;
    public String max_count;
    public String deadline;
    public String today;
    public String UID_dash;
    public int heart_num;
    public int parti_num;
    public int state;
    public int count;





    public Form(){ };
    public Form( String FID, String UID_dash,String image, String subject, String text, String address,String category_text, String cost, String max_count,String deadline, String today){
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
    public String getCategory_text(){ return category_text; }
    public void setCategory_text(String category_text) {this.category_text = category_text;
    }
    public String getMax_count(){ return max_count; }
    public void setMax_count(String max_count) {this.max_count = max_count;
    }
    public String getDeadline(){ return deadline; }
    public void setDeadline(String deadline) {this.deadline = deadline;
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



}
