package com.example.moamoa.ui.category;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityCategoryBinding;
import com.google.android.material.tabs.TabLayout;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int tabIdx = i.getIntExtra("tabIdx",0);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager .setCurrentItem(tabIdx, false);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(Color.rgb(0,0,0),Color.rgb(47,157,39));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_actionbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();
        if (curId == R.id.action_search) {    // 검색
            Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}