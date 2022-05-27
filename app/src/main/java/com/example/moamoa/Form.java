package com.example.moamoa;

public class Form {
   // String FID;
    String subject;
    String text;
    //String photo;
    String address;
    String category_text;
    int cost;
    int max_count;
    int deadline;
    int today;

    public Form(){ };
    public Form(   String subject, String text, String address,String category_text, int cost, int max_count,int deadline, int today){
        //this.FID = FID;
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
    public int getCost(){ return cost; }
    public void setCost(int cost) {this.cost = cost;
    }
    public String getCategory_text(){ return category_text; }
    public void setCategory_text(String category_text) {this.category_text = category_text;
    }
    public int getMax_count(){ return max_count; }
    public void setMax_count(int max_count) {this.max_count = max_count;
    }
    public int getDeadline(){ return deadline; }
    public void setDeadline(int deadline) {this.deadline = deadline;
    }
    public int getToday(){ return today; }
    public void setToday(int today) {this.today = today;
    }
}
