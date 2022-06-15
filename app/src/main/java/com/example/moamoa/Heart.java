package com.example.moamoa;

public class Heart{

    String FID;


    public Heart(){ };
    public Heart(String FID ){
        this.FID=FID;
    }
    //   public void setUID(String FID){
    //      this.FID = FID;
    //   }

    public String getFID(){
        return FID;
    }
    public void setFID(String FID){
        this.FID = FID;
    }

}
