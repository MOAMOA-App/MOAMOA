package com.example.moamoa.ui.mypage;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moamoa.LoginActivity;
import com.example.moamoa.MainActivity;
import com.example.moamoa.R;
//import com.example.moamoa.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class Setting extends AppCompatActivity {

    static final String[] LIST_MENU = {"알림 설정", "앱 정보", "로그아웃", "회원 탈퇴"};
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListView listview = (ListView) findViewById(R.id.set_listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "메세지", Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    //string 부분 틀림
//                    Intent intent = new Intent((EditMyinfo.class));
//                    startActivity(intent);
                }
                if (position == 1) {
//                    Intent intent = new Intent(String.valueOf(EditMyinfo.class));
//                    startActivity(intent);
                }
                if (position == 2) {

                    auth.signOut();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    //계정정보 삭제
                    //삭제에 딜레이가 있음
                    new AlertDialog.Builder(Setting.this)// TestActivity 부분에는 현재 Activity의 이름 입력.
                            .setMessage("정말 탈퇴하시겠습니까?")//제목부분(직접작성)
                            .setPositiveButton("확인",new DialogInterface.OnClickListener(){      //버튼1(직접작성)
                                public void onClick(DialogInterface dialog,int which){
                                    //데이터베이스 정보 삭제
                                    mDatabase.child("Users").child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplication(), "1삭제 성공", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplication(), "1삭제 실패...", Toast.LENGTH_SHORT).show();
                                            System.out.println("error: "+e.getMessage());
                                        }
                                    });
                                    //계정 정보 삭제(데이터베이스와 별도)
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplication(), "2삭제 성공", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "User account deleted.");
                                            }else {
                                                Toast.makeText(getApplication(), "2삭제 실패...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    //기기에 저장된 세션때문에 로그아웃이 안됨.
                                    //토큰 취소하여 로그아웃되도록 함.
//                                    auth.signOut();
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("취소",new DialogInterface.OnClickListener(){//버튼2(직접 작성)
                                public void onClick(DialogInterface dialog, int which){
                                }
                            })
                            .show();
                }
            }
        });
    }
}

