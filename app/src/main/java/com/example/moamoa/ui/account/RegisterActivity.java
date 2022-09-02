package com.example.moamoa.ui.account;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moamoa.LoginActivity;
import com.example.moamoa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Random_nick random_nicks;
    private static final String TAG = "RegisterActivity";
    EditText ConfirmText, PasswordText, PasswordcheckText, NameText;
    Button registerBtn;
    private FirebaseAuth firebaseAuth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //액션 바 등록하기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("회원가입");

        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기버튼
        actionBar.setDisplayShowHomeEnabled(true); //홈 아이콘

        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth =  FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();


        ConfirmText = findViewById(R.id.confirmEdt);
        PasswordText = findViewById(R.id.passwordEdt);
        PasswordcheckText = findViewById(R.id.passwordcheckEdt);
        registerBtn = findViewById(R.id.register_reg);
        NameText = findViewById(R.id.nameEt);


        //파이어베이스 user 로 접글
        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                AlertDialog.Builder alerting = new AlertDialog.Builder(RegisterActivity.this);
                //가입 정보 가져오기
                String name = NameText.getText().toString().trim();
                final String comfirm = ConfirmText.getText().toString().trim();
                String pwd = PasswordText.getText().toString().trim();
                String pwdcheck = PasswordcheckText.getText().toString().trim();
                boolean eb = comfirm.contains("@") && comfirm.contains(".");
                if(name.equals("")){
                    alerting.setMessage("아이디를 입력해주세요");
                    alerting.show();
                }else if(comfirm.equals("")){
                    alerting.setMessage("이메일이나 휴대폰 번호를 입력해주세요");
                    alerting.show();
                }else if(pwd.equals("")){
                    alerting.setMessage("비밀번호를 입력해주세요");
                    alerting.show();
                }else if(pwdcheck.equals("")){
                    alerting.setMessage("비밀번호 확인을 입력해주세요");
                    alerting.show();
                }else if(pwd.length()<10){
                    alerting.setMessage("비밀번호가 너무 짧아요");
                    alerting.show();
                }else if(!pwd.equals(pwdcheck)){
                    alerting.setMessage("비밀번호와 비밀번호 확인이 같지 않아요");
                    alerting.show();
                }else {
                    Log.d(TAG, "등록 버튼 " + comfirm + " , " + pwd);

                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();
                    if(eb){
                        //파이어베이스에 신규계정 등록하기
                        firebaseAuth.createUserWithEmailAndPassword(comfirm, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //가입 성공시
                                if (task.isSuccessful()) {
                                    mDialog.dismiss();

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String email = user.getEmail();
                                    String uid = user.getUid();
                                    String name = NameText.getText().toString().trim();

                                    //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                    HashMap<String,Object> childUpdates = new HashMap<>();
                                    HashMap<Object,String> postValues = new HashMap<>();
                                    postValues.put("type","email");
                                    postValues.put("name",name);
                                    random_nicks = new Random_nick();
                                    random_nicks.setNickname();
                                    postValues.put("nick",random_nicks.getNickname());
                                    postValues.put("image","profile/"+random_nicks.getImage()+".png");
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("users");
                                    childUpdates.put(uid, postValues);
                                    reference.updateChildren(childUpdates);
                                    //가입이 이루어져을시 가입 화면을 빠져나감.
                                    user.sendEmailVerification();
                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    alerting.setMessage("입력하신 이메일의 인증 메일이 발송되었습니다")
                                            .setNeutralButton("확인",new DialogInterface.OnClickListener(){
                                                public void onClick(DialogInterface dialog, int which){
                                                    startActivity(intent);
                                                    finish();
                                                } }).create().show();




                                }else {
                                    // If sign in fails, display a message to the user.
                                    mDialog.dismiss();
                                    String temp = task.getException().toString();
                                    int numb = temp.indexOf(":");
                                    temp = temp.substring(numb+2);
                                    Log.w(TAG, "createUserWithEmail:failure"+temp);
                                    if(temp.equals("The email address is already in use by another account.")){
                                        alerting.setMessage("이미 가입된 이메일입니다.");
                                        alerting.show();
                                    }

                                    return;
                                }

                            }
                        });
                    }else{
                        Intent intent = new Intent(RegisterActivity.this, PhoneActivity.class);
                        String phone = comfirm;
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                    }

                }
            }
        });

    }


    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}