package com.example.moamoa.ui.acount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.moamoa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends Activity {
    private static final String TAG = "PhoneAuthActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        Intent intent = getIntent();
        String phoneNumber = "+82"+intent.getStringExtra("phone");
        TextView phonetxt = (TextView) findViewById(R.id.phone_auth_et_phone_num);
        phonetxt.setText(phoneNumber);


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("kr");
        // [END initialize_auth]


        Button authbtn = (Button) findViewById(R.id.phone_auth_btn_auth);//인증하기 버튼
        Button auth_checkbtn = (Button) findViewById(R.id.phone_auth_check_btn);//인증확인 버튼
        Button consentbtn = (Button) findViewById(R.id.phone_auth_btn_consent);//회원가입 버튼
        // Initialize phone auth callbacks
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(90L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(PhoneActivity.this)                 // Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    // 전화번호는 확인 되었으나 인증코드를 입력해야 하는 상태
                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // 인증 ID를 어딘가에 저장
                        // ...
                        //Log.w("인증 아이디",mResendToken.toString());
                        mVerificationId = verificationId;
                        mResendToken = forceResendingToken;
                        // 위의 해당 화이트리스트 코드를 사용하여 로그인을 완료해야 합니다
                        PhoneActivity.this.enableUserManuallyInputCode();

                        TextView text_60s = (TextView) findViewById(R.id.phone_auth_alert);
                        text_60s.setVisibility(View.VISIBLE);
                        auth_checkbtn.setVisibility(View.VISIBLE);
                    }
                    // 번호인증 혹은 기타 다른 인증(구글로그인, 이메일로그인 등) 끝난 상태
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // credential으로 로그인
                        consentbtn.setBackgroundColor(R.color.main_green);
                        consentbtn.setEnabled(true);
                    }

                    // 번호인증 실패 상태
                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // ...
                        Toast.makeText(getApplicationContext(),"인증 실패", Toast.LENGTH_LONG).show();
                    }
                })
                .build();

        // [START phone_auth_callbacks]
        authbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 전화번호 인증코드 요청
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        auth_checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView codetxt = (TextView) findViewById(R.id.phone_auth_et_auth_num);
                verifyPhoneNumberWithCode(mVerificationId,codetxt.getText().toString());
            }
        });
    }

    private void enableUserManuallyInputCode() {
        // No-op
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // [END resend_verification]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
