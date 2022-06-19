package com.example.moamoa.ui.mypage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.moamoa.LoginActivity;
import com.example.moamoa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;

public class CustomDialog extends Dialog {
    private ImageView profile;
    private Button okClick;
    private Button closeClick;
    private Button defaultClick;
    private DatabaseReference mDatabase;
    private String dimage;
    Uri file;
    private static final int REQUEST_CODE = 0;

    public CustomDialog(@NonNull Context context, String contents) {
        super(context);
        setContentView(R.layout.profileimage);

        profile = findViewById(R.id.profileimageView);
        okClick = findViewById(R.id.okbtn);
        closeClick = findViewById(R.id.close);
        defaultClick = findViewById(R.id.defaultimage);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //변경 전 현재 이미지 표시
        if (user != null) {
            mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();
                    StorageReference pathReference = storageReference.child("profile");

                    if (pathReference == null) {
                        dismiss();
                    } else {
                        String dimage = snapshot.child("image").getValue().toString();
                        StorageReference submitProfile = storageReference.child(dimage);
                        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(profile.getContext())
                                        .load(uri)
                                        .into(profile);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "데이터를 가져오는데 실패했습니다", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                    Toast.makeText(getContext(), "데이터를 가져오는데 실패했습니다", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            // No user is signed in
        }

        //기본이미지로 버튼 클릭
        defaultClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                StorageReference pathReference = storageReference.child("profile");

                if (pathReference == null){
                    dismiss();
                }else {
                    int randomNum1 = (int) (Math.random() * 4);
                    int randomNum2 = (int) (Math.random() * 10);
                    dimage = "profile/"+randomNum1+"_"+randomNum2+".png";
                    StorageReference submitProfile = storageReference.child(dimage);
                    submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(profile.getContext())
                                    .load(uri)
                                    .into(profile);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"데이터를 가져오는데 실패했습니다" , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        //확인버튼 클릭
        okClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //기본 이미지 변경
                if(dimage == null){
                    dismiss();
                }
                else {
                    mDatabase.child("users").child(user.getUid()).child("image").setValue(dimage);
                    dismiss();
                }
            }
        });

        //취소버튼 클릭
        closeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //이미지 버튼, 갤러리로 이동
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//                if (!hasWritePerm) { // 권한 없을 시  권한설정 요청
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                }
//                else
//                loadAlbum();
            }
        });

    }

}