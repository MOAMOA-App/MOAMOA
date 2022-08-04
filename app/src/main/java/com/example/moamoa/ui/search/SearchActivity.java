package com.example.moamoa.ui.search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityChatsBinding;
import com.example.moamoa.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    SearchFragment searchFragment = new SearchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance())
                    .commitNow();
        }

         */

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        // 프래그먼트 매니저로 searchcontainer에 searchFragment 연결해줌
        //getSupportFragmentManager().beginTransaction().replace(R.id.chatscontainer, searchFragment).commit();
    }
}