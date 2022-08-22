package com.example.moamoa.ui.mypage;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moamoa.R;
import com.example.moamoa.ui.account.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;


public class CheckMyinfo extends AppCompatActivity {
    TextView name;
    TextView type;
    Button Btn;
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_myinfo);

        name = findViewById(R.id.myinfo_name);
        type = findViewById(R.id.myinfo_type);
        Btn = findViewById(R.id.myinfo_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();;

        if (user != null) {
            // User is signed in
            //프로필 정보
            mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User User = snapshot.getValue(User.class);
                    name.setText(User.getname());
                    String Type = User.getType();
                    if(Type.equals("google")){
                        type.setTextColor(Color.parseColor("#0000FF")); //색상코드 이용(파랑)
                    }
                    if(Type.equals("naver")){
                        type.setTextColor(Color.parseColor("#008000")); //색상코드 이용(초록)
                    }
                    if(Type.equals("kakao")){
                        type.setTextColor(Color.parseColor("#ffff00")); //색상코드 이용(노랑)
                    }
                    type.setText(Type);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                    Toast.makeText(getApplication(), "데이터를 가져오는데 실패했습니다", Toast.LENGTH_LONG).show();
                }
            });
        }

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
