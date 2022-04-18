package com.example.moamoa.ui.chatlist;

import java.util.ArrayList;

public class ChatListData {
    String formname;
    String recentmessage;

    public ChatListData(String formname, String recentmessage) {
        this.formname = formname;
        this.recentmessage= recentmessage;
    }

    public String getMessage() {
        return recentmessage;
    }

    public String getName() {
        return formname;
    }

    public void setMessage(String recentmessage) { this.recentmessage = recentmessage; }

    public void setName(String formname) {
        this.formname = formname;
    }

    public static ArrayList<ChatListData> createContactsList(int numContacts) {
        ArrayList<ChatListData> contacts = new ArrayList<ChatListData>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new ChatListData("FORMNAME","RECENTMESSAGE"));
        }
        return contacts;
    }
}