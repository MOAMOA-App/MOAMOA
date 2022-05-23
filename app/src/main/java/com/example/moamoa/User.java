package com.example.moamoa;

public class User {
    String UID;
    String name;
    String nick;
    String profile_img;
    String local;

    public User(){ };
    public User(String UID,String name, String nick, String profile_img, String local){
        this.UID = UID;
        this.name = name;
        this.nick = nick;
        this.profile_img = profile_img;
        this.local = local;
    }
    public void setUID(String UID){
        this.UID = UID;
    }
    public String getname(){
        return name;
    }
    public void setname(String name){
        this.name = name;
    }
    public String getnick(){
        return nick;
    }
    public void setnick(String nick){
        this.nick = nick;
    }
    public String getprofile_img(){
        return profile_img;
    }
    public void setprofile_img(String profile_img){
        this.profile_img = profile_img;
    }
    public String getlocal(){
        return local;
    }
    public void setlocal(String local){
        this.local = local;
    }
}
