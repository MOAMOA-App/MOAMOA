package com.example.moamoa.ui.chats;

import java.util.ArrayList;

public class ChatsData {
    String leftname;
    String leftmessage;
    String rightname;
    String rightmessage;

    public ChatsData(String leftname, String leftmessage, String rightname, String rightmessage) {
        this.leftname = leftname;
        this.leftmessage= leftmessage;
        this.rightname = rightname;
        this.rightmessage= rightmessage;
    }

    public String getLeftName() { return leftname; }
    public void setLeftName(String leftnickname) { this.leftname = leftnickname; }

    public String getLeftMessage() { return leftmessage; }
    public void setLeftMessage(String leftmessage) { this.leftmessage = leftmessage; }

    public String getRightName() { return rightname; }
    public void setRightName(String rightnickname) { this.rightname = rightnickname; }

    public String getRightMessage() { return rightmessage; }
    public void setRightMessage(String rightmessage) { this.rightmessage = rightmessage; }

    public static ArrayList<ChatsData> createContactsList(int numContacts) {
        ArrayList<ChatsData> contacts = new ArrayList<ChatsData>();

        return contacts;
    }
}