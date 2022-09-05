package com.example.moamoa.ui.mypage;

import static android.content.ContentValues.TAG;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.moamoa.BuildConfig;
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
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomDialog extends Activity {
    private ImageView profile;
    private Button okClick;
    private Button closeClick;
    private Button defaultClick;
    private DatabaseReference mDatabase;
    private String dimage;
    private FirebaseStorage storage;
    private static final int PICK_FROM_ALBUM = 1; //앨범에서 사진 가져오기

    private File tempFile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //출처: https://soohyun6879.tistory.com/76 [코딩기록:티스토리]

        //타이틀바없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profileimage);

        profile = findViewById(R.id.profileimageView);
        okClick = findViewById(R.id.okbtn);
        closeClick = findViewById(R.id.close);
        defaultClick = findViewById(R.id.defaultimage);

        storage = FirebaseStorage.getInstance();
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
                        finish();
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
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                    Toast.makeText(CustomDialog.this, "데이터를 가져오는데 실패했습니다", Toast.LENGTH_LONG).show();
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

                if (pathReference == null) {
                    finish();
                } else {
                    int randomNum1 = (int) (Math.random() * 4);
                    int randomNum2 = (int) (Math.random() * 10);
                    dimage = "profile/" + randomNum1 + "_" + randomNum2 + ".png";
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
                            Toast.makeText(CustomDialog.this, "데이터를 가져오는데 실패했습니다", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        //확인버튼 클릭
        okClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dimage == null) {
                    finish();
                }
                //갤러리에서 가져온 이미지
                if (dimage.contains("userprofile/")){
                    mDatabase.child("users").child(user.getUid()).child("image").setValue(dimage);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 4000); //딜레이 타임 조절 //4초
                    //파이어베이스에 이미지를 저장하고 불러오는데 시간이 오래 걸린다.
                }
                //기본 이미지 설정
                else {
                    mDatabase.child("users").child(user.getUid()).child("image").setValue(dimage);

                    //갤러리에서 받아온 이전 이미지 삭제
                    StorageReference storageRef = storage.getReference();
                    StorageReference desertRef = storageRef.child("userprofile/"+"profile_"+ user.getUid()+".jpeg");

                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 3000); //딜레이 타임 조절 //3초
                }
            }
        });

        //취소버튼 클릭
        closeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //이미지 버튼, 갤러리로 이동
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //cancel 버튼
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            //tempFile == 크롭으로 넘어가자마자 생기는 임시 크롭 이미지 파일
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }

        //done 버튼 누르기 전
        switch (requestCode) {
            //앨범에서 이미지 가져옴
            case PICK_FROM_ALBUM: {
                Uri photoUri = data.getData(); //가져온 이미지

                //갤러리에서 선택한 경우에는 tempFile 이 없으므로 새로 생성해줍니다.
                if (tempFile == null) {
                    try {
                        tempFile = createImageFile();  //temp_.jpg 파일 생성
                    } catch (IOException e) {
                        Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                        e.printStackTrace();
                    }
                }

                //크롭 후 저장할 Uri
                //파일을 Uri로 변경
                Uri savingUri = Uri.fromFile(tempFile);

                //원본 이미지 photoUri에서 크롭하여 savingUri로
                Crop.of(photoUri, savingUri).asSquare().start(this);

                break;
            }

            //done 누르면
            case Crop.REQUEST_CROP: {
                //tempfile을 Bitmap으로 변경
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

                //bitmap으로 변경된 이미지를 profile에 setimage
                profile.setImageBitmap(originalBm);

                //bitmap을 Uri로
                Uri uri = getImageUri(this,originalBm);

                createProfile(uri);
                dimage = "userprofile/"+"profile_"+ user.getUid() +".jpeg";

                /**
                 *  tempFile 사용 후 null 처리를 해줘야 합니다.
                 *  (resultCode != RESULT_OK) 일 때 (tempFile != null)이면 해당 파일을 삭제하기 때문에
                 *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
                 */

                tempFile = null;
            }

        }

    }

    //폴더 및 파일 만들기
    private File createImageFile() throws IOException {

        String imageFileName = "temp_";

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg");

        return image;
    }

    //Bitmap -> Uri로 변경 (bitmap 압축 코드 포함)
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //Bitmap 압축 함수
        //compress() 의 두번째 파라메터로 40 을 넘기고있는데 이건 40%로 압축한다는 의미입니다.
        inImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        //없으면 바이트가 줄지 않음
        byte[] byteArray = bytes.toByteArray();
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), compressedBitmap, "Title", null);
        return Uri.parse(path);
    }

    //데이터베이스에 사진 저장하기
    private void createProfile(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storageRef = storage.getReference();

        String filename = "profile_" + user.getUid() + ".jpeg";

        StorageReference riversRef = storageRef.child("userprofile/"+filename);
        UploadTask uploadTask;
        uploadTask = riversRef.putFile(uri);

        //이전 이미지 삭제
        StorageReference desertRef = storageRef.child("userprofile/"+"profile_"+ user.getUid()+".jpeg");

        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        //새로운 프로필 이미지 저장
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

    }

}
//파이어베이스에 사진이 저장되고 불러오는데 시간이 오래 걸림...

//출처 : https://black-jin0427.tistory.com/123
//bitmap 압축 : https://hello-bryan.tistory.com/69