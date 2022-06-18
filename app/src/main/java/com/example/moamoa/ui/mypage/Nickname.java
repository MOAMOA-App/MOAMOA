package com.example.moamoa.ui.mypage;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.moamoa.R;
import com.example.moamoa.ui.account.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Nickname extends Dialog {
    private TextView txt_contents;
    private Button okClick;
    private Button cancelClick;
    private DatabaseReference mDatabase;

    public Nickname(@NonNull Context context, String contents) {
        super(context);
        setContentView(R.layout.nickname);
        TextView editnick = findViewById(R.id.editnick);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        okClick = findViewById(R.id.close);
        cancelClick = findViewById(R.id.cancel);

        //변경 전 현재 닉네임 표시
        mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User User = snapshot.getValue(User.class);
                editnick.setText(User.getnick());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                Toast.makeText(getContext(),"데이터를 가져오는데 실패했습니다" , Toast.LENGTH_LONG).show();
            }
        });

        //확인 버튼
        okClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = editnick.getText().toString().trim();
                mDatabase.child("users").child(user.getUid()).child("nick").setValue(nick);
                dismiss();
            }
        });

        //취소 버튼
        cancelClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
