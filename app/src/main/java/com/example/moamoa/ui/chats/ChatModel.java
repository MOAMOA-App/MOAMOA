package com.example.moamoa.ui.chats;

import android.util.Log;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    String MY_ID;
    String THEIR_ID;
    String Chatting_room_id;
    String last_message_time;
    String last_message;
    String last_message_id;

    ChatModel(){
        this.MY_ID = null;
        this.THEIR_ID = null;
        this.last_message_time = null;
        this.last_message = null;
        this.last_message_id = null;
    }
}
