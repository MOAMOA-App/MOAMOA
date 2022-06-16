package com.example.moamoa.ui.category;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.moamoa.databinding.ParticipatedFormsBinding;
import com.example.moamoa.ui.category.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ParticipatedForms extends AppCompatActivity {
    private ParticipatedFormsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ParticipatedFormsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs2;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(Color.rgb(0,0,0),Color.rgb(47,157,39));

    }
}
