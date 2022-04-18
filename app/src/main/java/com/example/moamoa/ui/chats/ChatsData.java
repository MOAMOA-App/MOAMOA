package com.example.moamoa.ui.chats;

import java.util.ArrayList;

public class ChatsData {
    String nickname;
    String message;

    public ChatsData(String nickname, String message) {
        this.nickname = nickname;
        this.message= message;
    }

    public String getName() {
        return nickname;
    }

    public void setName(String nickname) {
        this.nickname = nickname;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) { this.message = message; }


    public static ArrayList<ChatsData> createContactsList(int numContacts) {
        ArrayList<ChatsData> contacts = new ArrayList<ChatsData>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new ChatsData("NICKNAME","MESSAGE"));
        }
        return contacts;
    }
}