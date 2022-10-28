package com.example.moamoa.ui.mypage;

        import android.app.Activity;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListAdapter;
        import android.widget.ListView;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.viewpager.widget.ViewPager;

        import com.example.moamoa.databinding.CreatedFormsBinding;
        import com.google.android.material.tabs.TabLayout;

        import java.util.ArrayList;

public class EmptyForms extends AppCompatActivity {

    private CreatedFormsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CreatedFormsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter2 sectionsPagerAdapter = new SectionsPagerAdapter2(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(Color.rgb(0,0,0),Color.rgb(47,157,39));


    }
}