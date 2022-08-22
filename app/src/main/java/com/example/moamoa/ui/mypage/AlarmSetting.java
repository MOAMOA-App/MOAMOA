package com.example.moamoa.ui.mypage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.moamoa.LoginActivity;
import com.example.moamoa.R;
import com.example.moamoa.ui.account.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AlarmSetting extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);

        //리스트뷰
        ListView listView;
        listView = findViewById(R.id.alarmset_listView);

        AlarmsetAdapter oAdapter = new AlarmsetAdapter();
        listView.setAdapter(oAdapter);


        // 첫 번째 아이템 추가.
        oAdapter.addItem("게시글이 변경되었을 경우 알림") ;
        // 두 번째 아이템 추가.
        oAdapter.addItem("찜한 게시글이 변경되었을 경우 알림") ;
        // 세 번째 아이템 추가.
        oAdapter.addItem("게시글의 거래가 성사되었을 경우 알림") ;
        // 네 번째 아이템 추가.
        oAdapter.addItem("새로운 공지사항이 발생했을 경우 알림") ;
        // 다섯 번째 아이템 추가.
        oAdapter.addItem("새로운 채팅이 생성되었을 경우 알림") ;
        // 여섯 번째 아이템 추가.
        oAdapter.addItem("사용자가 지정한 키워드를 포함한 게시물이 등록되었을 경우 알림") ;

       //하트 스위치 찾아보기

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(position==0){
//                    Intent intent = new Intent(AlarmSetting.this, ParticipatedForms.class);
//                    startActivity(intent);
//                }
//                if(position==1){
//                    Intent intent = new Intent(AlarmSetting.this, ParticipatedForms.class);
//                    startActivity(intent);
//                }
//                if(position==2){
//                    Intent intent = new Intent(AlarmSetting.this, ParticipatedForms.class);
//                    startActivity(intent);
//                }
//                if(position==3){
//
//                }
//                if(position==4){
//                    Intent intent = new Intent(AlarmSetting.this, Setting.class);
//                    startActivity(intent);
//                }
//
//            }
//        });
    }
}

