package com.example.moamoa.ui.notifications;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityNotificationsBinding;
import com.example.moamoa.ui.chats.ChatsFragment;

public class NotificationsActivity extends AppCompatActivity {
    private ActivityNotificationsBinding binding;
    NotificationsFragment notificationsFragment = new NotificationsFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        getSupportFragmentManager().beginTransaction().replace(R.id.alarmcontainer, notificationsFragment).commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}