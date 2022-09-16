package com.example.moamoa.ui.chats;

public class ChatsUserData {
    private String profilepic;
    private String nationality;
    private String usernick;
    private String account;
    private String UID;

    /*
    public ChatsUserData(String profilepic, String nationality, String usernick, String account, String UID){
        this.profilepic = profilepic;
        this.nationality = nationality;
        this.usernick = usernick;
        this.account = account;
        this.UID = UID;
    }
     */

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }


}
