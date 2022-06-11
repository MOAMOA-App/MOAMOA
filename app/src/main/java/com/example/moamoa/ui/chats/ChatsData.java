package com.example.moamoa.ui.chats;

import com.example.moamoa.ChatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatsData {
    String nickname;
    String chatmsg;
    String sendedtime;
    int leftpfimage;

    public ChatsData(){}

    public ChatsData(String nickname, String chatmsg) {
        this.nickname = nickname;
        this.chatmsg= chatmsg;
    }

    public String getLeftname() {
        return nickname;
    }

    public void setLeftname(String nickname) {
        this.nickname = nickname;
    }

    public String getLeftmessage() {
        return chatmsg;
    }

    public void setLeftmessage(String chatmsg) {
        this.chatmsg = chatmsg;
    }

    public int getLeftpfimage() {
        return leftpfimage;
    }

    public void setLeftpfimage(int leftpfimage) {
        this.leftpfimage = leftpfimage;
    }

    public static ArrayList<ChatsData> createContactsList(int numContacts) {
        ArrayList<ChatsData> contacts = new ArrayList<ChatsData>();

        return contacts;
    }
}