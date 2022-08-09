package com.example.moamoa.ui.account;

public class User {
    String UID;
    String name;
    // public으로 변경해서 채팅에서 씀
    public String nick;
    public String profile_img;
    int my_category;
    String local;
    String host;
    String parti;
    String heart_user;
    String area;
    int num;


    public User(){ };
    public User(String UID,String name, String nick, String profile_img, String local,int num){
        this.UID = UID;
        this.name = name;
        this.nick = nick;
        this.profile_img = profile_img;
        this.local = local;
        this.num=num;
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
    public int getnum(){
        return num;
    }
    public void setnum(int num){
        this.num = num;
    }
    public String getHost(){
        return host;
    }
    public void setHost(String host){
        this.host = host;
    }
    public String getArea(){ return area; }
    public void setArea(String area){ this.area = area; }
    public String getparti(){
        return parti;
    }
    public void setparti(String parti){ this.parti = parti; }
    public String getheart_user(){
        return heart_user;
    }
    public void setheart_user(String heart_user){
        this.heart_user = heart_user;
    }
    public int getMy_category(){
        return my_category;
    }
    public void setMy_category(int my_category){
        this.my_category = my_category;
    }

}
