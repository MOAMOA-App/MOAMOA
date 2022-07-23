package com.example.moamoa.ui.notifications;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityNotificationsBinding;

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

    }
}