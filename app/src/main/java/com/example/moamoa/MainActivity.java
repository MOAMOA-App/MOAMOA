package com.example.moamoa;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.moamoa.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;    //Activity_main + binding
    ArrayList<formlist_data> formlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //========================하단 네비게이션=====================================
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host);
        //AppBarConfiguration -> 앱바에 라벨과 홈제외 뒤로가기 버튼 출력
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //앱바에 객체 적용
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //navController activitymain의 navView에 적용
        NavigationUI.setupWithNavController(binding.navView, navController);

        //========================리스트 뷰=====================================
        this.InitializeFormData();

        ListView listView = (ListView) findViewById(R.id.listview);
        final formlist_adapter formlistAdapter = new formlist_adapter(this,formlist);
        listView.setAdapter(formlistAdapter);
    }
    public void InitializeFormData()
    {
        formlist = new ArrayList<formlist_data>();

        formlist.add(new formlist_data(1, "30,000","제목1","이름1","22-03-12"));
        formlist.add(new formlist_data(1, "30,000","제목2","이름1","22-03-12"));
        formlist.add(new formlist_data(1, "30,000","제목3","이름1","22-03-12"));
    }
    public void buttonClick(View view) {
    }
}