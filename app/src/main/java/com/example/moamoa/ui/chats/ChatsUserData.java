package com.example.moamoa.ui.chats;

public class ChatsUserData {
    String profilepic;
    String nationality;
    String usernick;
    String account;

    public ChatsUserData(String profilepic, String usernick, String account){
        this.profilepic = profilepic;
        //this.nationality = nationality;
        this.usernick = usernick;
        this.account = account;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }

}
