package com.example.moamoa;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    public Map<String, Boolean> users = new HashMap<>(); // 채팅방 유저들
    public Map<String, Comment> comments = new HashMap<>(); // 채팅방 대화내용

    public static class Comment{
        public String UID;
        public String message;
    }
}
