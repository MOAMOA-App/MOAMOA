package com.example.moamoa.ui.mypage;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.moamoa.databinding.CreatedFormsBinding;
import com.example.moamoa.databinding.ParticipatedFormsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class ParticipatedForms extends AppCompatActivity {
    private ParticipatedFormsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ParticipatedFormsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter2 sectionsPagerAdapter = new SectionsPagerAdapter2(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(Color.rgb(0,0,0),Color.rgb(47,157,39));

    }
}
