package com.example.moamoa.ui.chats;

import java.util.ArrayList;

public class ChatsData {
    String leftnickname;
    String leftmessage;
    String rightnickname;
    String rightmessage;

    public ChatsData(String nickname, String message) {
        this.leftnickname = leftnickname;
        this.leftmessage= leftmessage;
        this.rightnickname = rightnickname;
        this.rightmessage= rightmessage;
    }

    public String getName() {
        return leftnickname;
    }

    public void setName(String leftnickname) {
        this.leftnickname = leftnickname;
    }


    public String getMessage() {
        return leftmessage;
    }

    public void setMessage(String leftmessage) { this.leftmessage = leftmessage; }


    public static ArrayList<ChatsData> createContactsList(int numContacts) {
        ArrayList<ChatsData> contacts = new ArrayList<ChatsData>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new ChatsData("NICKNAME","MESSAGE"));
        }
        return contacts;
    }
}