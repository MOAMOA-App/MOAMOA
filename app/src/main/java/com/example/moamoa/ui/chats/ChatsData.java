package com.example.moamoa.ui.chats;

import java.util.ArrayList;

public class ChatsData {
    String leftnickname;
    String leftmessage;
    String rightnickname;
    String rightmessage;
    int profileimageleft;
    int profileimageright;
    int nationalityleft;
    int nationalityright;

    public ChatsData(String leftnickname, String leftmessage, String rightnickname, String rightmessage) {
        this.leftnickname = leftnickname;
        this.leftmessage= leftmessage;
        this.rightnickname = rightnickname;
        this.rightmessage= rightmessage;
    }

    public String getLeftName() {
        return leftnickname;
    }
    public void setLeftName(String leftnickname) {
        this.leftnickname = leftnickname;
    }

    public String getRightName() {
        return rightnickname;
    }
    public void setRightName(String rightnickname) {
        this.rightnickname = rightnickname;
    }

    public String getLeftMessage() {
        return leftmessage;
    }
    public void setLeftMessage(String leftmessage) { this.leftmessage = leftmessage; }

    public String getRightMessage() {
        return rightmessage;
    }
    public void setRightMessage(String rightmessage) { this.rightmessage = rightmessage; }



    public static ArrayList<ChatsData> createContactsList(int numContacts) {
        ArrayList<ChatsData> contacts = new ArrayList<ChatsData>();
        return contacts;
    }
}