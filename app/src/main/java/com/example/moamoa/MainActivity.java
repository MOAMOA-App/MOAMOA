package com.example.moamoa;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.moamoa.databinding.ActivityMainBinding;
import com.example.moamoa.ui.dashboard.DatePickerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;    //Activity_main + binding
    private FirebaseAuth mAuth;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd");
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        return mFormat.format(mDate);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
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

        //추가
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
    // [END auth_with_google]
    @Override
    protected void onStart() {
        super.onStart();
        /*
        int today= Integer.parseInt(getTime());
        FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Form listData = snapshot.getValue(Form.class);

                    if (Integer.parseInt(listData.deadline.substring(0,8)) < today){
                        FirebaseDatabase.getInstance().getReference("form").child(listData.FID).child("state").setValue(2);
                    }
                    else if (Integer.parseInt(listData.getMax_count())-listData.getparti_num()<=2 && Integer.parseInt(listData.deadline) == today){
                        FirebaseDatabase.getInstance().getReference("form").child(listData.FID).child("state").setValue(1);
                    }
                    else if(Integer.parseInt(listData.getMax_count())-listData.getparti_num()==0){
                        FirebaseDatabase.getInstance().getReference("form").child(listData.FID).child("state").setValue(1);
                    }
                    else if (Integer.parseInt(listData.getMax_count())-listData.getparti_num()>2 && Integer.parseInt(listData.deadline) >= today)
                    {
                        FirebaseDatabase.getInstance().getReference("form").child(listData.FID).child("state").setValue(0);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
       */
    }
//캘린더

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        if (day<10){
           day_string = "0" + Integer.toString(day);
        }
        if (month<10){
            month_string = "0" + Integer.toString(month+1);
        }

        String dateMessage = (year_string  +month_string + day_string  );
        TextView tv = findViewById(R.id.deadline);
        tv.setText(year_string  +"/"+month_string +"/"+ day_string);

    }
//캘린더
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();

            moveTaskToBack(true); // 태스크를 백그라운드로 이동
            finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid()); // 앱 프로세스 종료
        }
    }

    // 액션바 버튼추가
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_actionbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();
        switch(curId){
            case R.id.action_search:    // 검색
                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_alarm:     // 알림
                Toast.makeText(this, "Alarm Clicked", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

     */
}