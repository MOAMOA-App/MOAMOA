package com.example.moamoa.ui.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moamoa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class CheckLogin extends AppCompatActivity {
    EditText checkpwtext;
    Button checkloginBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_login);

        checkpwtext = findViewById(R.id.checkpwtext);
        checkloginBtn = findViewById(R.id.checkloginbtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth auth = FirebaseAuth.getInstance();


        checkloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ppwd = checkpwtext.getText().toString().trim();

                auth.signInWithEmailAndPassword(user.getEmail(), ppwd)
                        .addOnCompleteListener(CheckLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplication(), "로그인 성공", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(CheckLogin.this, EditMyinfo.class);
                                    intent.putExtra("ppwd", ppwd);
                                    startActivity(intent);
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getApplication(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
