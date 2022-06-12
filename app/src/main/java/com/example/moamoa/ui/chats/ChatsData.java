package com.example.moamoa.ui.chats;

import java.util.ArrayList;

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

    public String getSendedtime() {
        return sendedtime;
    }

    public void setSendedtime(String sendedtime) {
        this.sendedtime = sendedtime;
    }


    public static ArrayList<ChatsData> createContactsList(int numContacts) {
        ArrayList<ChatsData> contacts = new ArrayList<ChatsData>();

        return contacts;
    }
}