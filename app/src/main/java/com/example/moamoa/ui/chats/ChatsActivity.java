package com.example.moamoa.ui.chats;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatsBinding;

public class ChatsActivity extends AppCompatActivity {

    private FragmentChatsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chats);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}