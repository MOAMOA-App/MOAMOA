package com.example.moamoa.ui.acount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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


        Button authbtn = (Button) findViewById(R.id.phone_auth_btn_auth);
        Button consentbtn = (Button) findViewById(R.id.phone_auth_btn_consent);
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        authbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 전화번호 인증코드 요청
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(90L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(PhoneActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(String verificationId,
                                                   PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                // Save the verification id somewhere
                                // ...

                                // The corresponding whitelisted code above should be used to complete sign-in.
                                PhoneActivity.this.enableUserManuallyInputCode();
                            }

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                // Sign in with the credential
                                // ...
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                // ...
                            }
                        })
                        .build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        consentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView codetxt = (TextView) findViewById(R.id.phone_auth_et_auth_num);
                verifyPhoneNumberWithCode(phoneNumber,codetxt.getText().toString());
            }
        });
    }

    private void enableUserManuallyInputCode() {
        // No-op
    }

    //코드가 있는 전화번호 확인
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
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
