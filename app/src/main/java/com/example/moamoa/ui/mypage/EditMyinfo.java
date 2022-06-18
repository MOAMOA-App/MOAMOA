package com.example.moamoa.ui.mypage;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moamoa.LoginActivity;
import com.example.moamoa.R;
//import com.example.moamoa.User;
import com.example.moamoa.ui.acount.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMyinfo extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText PresentPasswordText, PasswordText, PasswordcheckText;
    Button passwordBtn;
    private int success = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_myinfo);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (user != null) {
            // User is signed in
            // Name, email address, and profile photo Url
            //프로필 정보
            TextView name = findViewById(R.id.name);
            TextView Email = findViewById(R.id.editTextTextEmailAddress);

            //nikname은 auth(계정 정보)에 들어가지 않으므로 database getReference()를 이용
            //닉네임 뜨는데 딜레이가 있음
            mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User User = snapshot.getValue(User.class);
                    name.setText(User.getname());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                    Toast.makeText(getApplication(),"데이터를 가져오는데 실패했습니다" , Toast.LENGTH_LONG).show();
                }
            });
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            Email.setText(email);


            //비밀번호 변경
            passwordBtn = findViewById(R.id.password_button);
            PresentPasswordText = findViewById(R.id.presentpassword);
            PasswordText = findViewById(R.id.newpassword);
            PasswordcheckText = findViewById(R.id.passwordchk);
            TextView pwdtext = findViewById(R.id.pwdtext);

            passwordBtn.setOnClickListener(new View.OnClickListener() {
                private int success;

                @Override
                public synchronized void onClick(View v) {
                    ProgressDialog mDialog = new ProgressDialog(EditMyinfo.this);
                    AlertDialog.Builder alerting = new AlertDialog.Builder(EditMyinfo.this);
                    //가입 정보 가져오기
                    String useremail = user.getEmail();
                    String ppwd = PresentPasswordText.getText().toString().trim();
                    login(useremail,ppwd);

                    String newpwd = PasswordText.getText().toString().trim();
                    String pwdcheck = PasswordcheckText.getText().toString().trim();


                    //재로그인
//                    auth.signInWithEmailAndPassword(useremail,ppwd)
//                            .addOnCompleteListener(EditMyinfo.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        Toast.makeText(getApplication(), "로그인 성공", Toast.LENGTH_LONG).show();
//                                        success = 1;
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        Toast.makeText(getApplication(), "로그인 실패", Toast.LENGTH_LONG).show();
//                                        success = 0;
//                                    }
//                                }
//                            });


                    if (ppwd.equals("")) {
                        alerting.setMessage("현재 비밀번호를 입력해주세요");
                        alerting.show();
                    }else if (newpwd.equals("")) {
                        alerting.setMessage("새 비밀번호를 입력해주세요");
                        alerting.show();
                    } else if (pwdcheck.equals("")) {
                        alerting.setMessage("비밀번호 확인을 입력해주세요");
                        alerting.show();
                    }else if (success == 0) {
                        alerting.setMessage("현재 비밀번호가 일치하지 않습니다.");
                        alerting.show();
                    }else if (newpwd.length() < 10) {
                        alerting.setMessage("비밀번호가 너무 짧습니다.");
                        alerting.show();
                    }else if (newpwd.equals(ppwd)) {
                        alerting.setMessage("현재 비밀번호와 같습니다.");
                        alerting.show();
                    } else if (!newpwd.equals(pwdcheck)) {
                        pwdtext.setText("*비밀번호가 일치하지 않습니다.");
                    } else if(!user.getEmail().equals("")) {
                        user.updatePassword(newpwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                    auth.signOut();
                                }
                            }
                        });
                        pwdtext.setText("");
                        Intent intent = new Intent(EditMyinfo.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplication(), "비밀번호가 변경되었습니다.\n다시 로그인해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            // No user is signed in
        }
    } // end of onCreate
    public int login(String useremail, String ppwd){
        //재로그인
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(useremail,ppwd)
                .addOnCompleteListener(EditMyinfo.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplication(), "로그인 성공", Toast.LENGTH_LONG).show();
                            success = 1;
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplication(), "로그인 실패", Toast.LENGTH_LONG).show();
                            success = 0;
                        }
                    }
                });
        return success;
    }
} // end of class
