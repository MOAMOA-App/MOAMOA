package com.example.moamoa.ui.chats;

import android.util.Log;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    public Map<String,Boolean> users = new HashMap<>(); // 채팅방 유저들
    String Chatting_room_id;
    String last_message_time;
    String last_message;
    String last_message_id;

    ChatModel(){

        this.last_message_time = null;
        this.last_message = null;
        this.last_message_id = null;
    }
}
