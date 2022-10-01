package com.example.moamoa.ui.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.moamoa.R;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Popup extends Dialog {
    private Button okClick;
    private Button cancelClick;

    public Popup(@NonNull Context context, String fid, String uid) {
        super(context);
        setContentView(R.layout.popup);
        TextView textnotice = findViewById(R.id.textnotice);
        textnotice.setText("글쓰기가 성공적으로 완료되었습니다.");

        okClick = findViewById(R.id.close);
        cancelClick = findViewById(R.id.cancel);
        cancelClick.setVisibility(View.GONE);

        //확인 버튼
        okClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FormdetailActivity.class);
                //입력한 input값을 intent로 전달한다.
                intent.putExtra("FID", fid);
                intent.putExtra(("UID_dash"),uid);

                getContext().startActivity(intent);
                dismiss();
            }
        });

    }
}
