package com.example.moamoa.ui.chatlist;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityChatlistBinding;

public class ChatListActivity extends AppCompatActivity {
    private ActivityChatlistBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        // 툴바 숨김 (activity_chatlist에서 직빵으로 연결해서 여기도 툴바있으면 두개나옴)
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
