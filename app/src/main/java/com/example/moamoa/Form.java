package com.example.moamoa;

public class Form {
   // String FID;
   public String subject;
   public  String text;
    public int photo;
    public  String address;
    public String category_text;
    public String cost;
    public String max_count;
    public String deadline;
    public String today;
    public String UID_dash;

    public Form(){ };
    public Form(  String UID_dash, String subject, String text, String address,String category_text, String cost, String max_count,String deadline, String today){
        //this.FID = FID;
        this.UID_dash=UID_dash;
        this.subject = subject;
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
}
