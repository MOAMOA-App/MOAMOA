package com.example.moamoa.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.moamoa.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ListupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        int tabIdx = i.getIntExtra("tabIdx",0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listup);
        ViewPager vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setCurrentItem(tabIdx, false);

        TabItem tab_Item2 =findViewById(R.id.tab_Item2);
        TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(vp);




    }
}
