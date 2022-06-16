package com.example.moamoa.ui.chats;

import android.util.Log;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    public String form_ID;
    public Map<String, Boolean> users = new HashMap<>(); // 채팅방 유저들
    public Map<String, Comment> comments = new HashMap<>();

    public static class Comment{
        String UID;
        String message;
    }

}