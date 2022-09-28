package com.example.moamoa.ui.formdetail;

public class PartyListData {
    private String UID;
    private String nickname;
    private String parti_date;
    private String lastchat;
    private String profile;

    public String getUID() {
        return this.UID;
    }

    public String getNickname() {
        return nickname;
    }

    public String getParti_date() {
        return parti_date;
    }

    public String getLastchat() {
        return lastchat;
    }

    public String getProfile(){
        return profile;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setParti_date(String parti_date) {
        this.parti_date = parti_date;
    }

    public void setLastchat(String lastchat) {
        this.lastchat = lastchat;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
