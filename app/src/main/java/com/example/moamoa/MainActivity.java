package com.example.moamoa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.moamoa.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;    //Activity_main + binding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        FirebaseUser a = FirebaseAuth.getInstance().getCurrentUser();
        if(a ==null){
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

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

    }
    public void buttonClick(View view) {
    }
}