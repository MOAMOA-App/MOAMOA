package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
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

public class DetailCreaterSideActivity extends Activity {

    private DatabaseReference mDatabase;
    private ImageView mainImage;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;

    private String image;
    private String temp;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailcreaterside);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mainImage = (ImageView) findViewById(R.id.mainImage);
        firebaseStorage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        temp = intent.getStringExtra("FID");

        Button chat_btn = (Button)findViewById(R.id.detail_chat_btn);   //채팅하기 버튼
        Button party_btn = (Button)findViewById(R.id.detail_party_btn); //참여하기 버튼
        //ImageButton heart_btn = (ImageButton) findViewById(R.id.detail_heart_btn);

        printpage();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void printpage(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("form");
        mDatabase.child(temp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String subject = dataSnapshot.child("subject").getValue().toString();
                String text = dataSnapshot.child("text").getValue().toString();
                String cost = dataSnapshot.child("cost").getValue().toString();
                String category = dataSnapshot.child("category_text").getValue().toString();
                String max_count = dataSnapshot.child("max_count").getValue().toString();
                String today = dataSnapshot.child("today").getValue().toString();
                String deadline = dataSnapshot.child("deadline").getValue().toString();
                String num_k= dataSnapshot.child("parti_num").getValue().toString() ;

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("category").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //변화된 값이 DataSnapshot 으로 넘어온다.
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            Log.e("", fileSnapshot.getValue().toString());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                image=dataSnapshot.child("image").getValue().toString() ;

                count=Integer.parseInt(dataSnapshot.child("count").getValue().toString());

                Log.d("확인","message상세 이미지 : "+count);
                String UID = dataSnapshot.child("UID_dash").getValue().toString();
                UserFind(UID);
                Initializeform(subject,category,text,cost,num_k+"/"+max_count,today,deadline);
                StorageReference pathReference = firebaseStorage.getReference(image);


                DetailCreaterSideActivity activity = (DetailCreaterSideActivity) mainImage.getContext();
                if (activity.isFinishing())
                    return;

                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(mainImage.getContext())
                                .load(uri)
                                .into(mainImage);

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    private void UserFind(String UID){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name;
                String profil_text;

                TextView name_tv = (TextView) findViewById(R.id.detail_nick);
                ImageView profile = (ImageView) findViewById(R.id.detail_profile);
                profil_text = dataSnapshot.child("image").getValue().toString();
                name = dataSnapshot.child("nick").getValue().toString();
                name_tv.setText(name);
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference pathReference = firebaseStorage.getReference(profil_text);

                DetailCreaterSideActivity activity1 = (DetailCreaterSideActivity) profile.getContext() ;
                if (activity1.isFinishing())
                    return;
                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(profile)
                                .load(uri)
                                .into(profile);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void Initializeform(String subject,String category,String text,String cost,String max_count,String today,String deadline) {
        TextView subject_text = (TextView) findViewById(R.id.detail_subject);
        TextView category_text = (TextView) findViewById(R.id.detail_category);
        TextView text_text = (TextView) findViewById(R.id.detail_textarea);
        TextView cost_text = (TextView) findViewById(R.id.detail_cost);
        TextView max_count_text = (TextView) findViewById(R.id.detail_party_num);
        TextView start = (TextView) findViewById(R.id.detail_startdate);

        TextView deadlines = (TextView) findViewById(R.id.detail_deadline);
        subject_text.setText(subject);
        text_text.setText(text);
        cost_text.setText(cost);
        category_text.setText(category);
        start.setText(today.toString().substring(0,4)+"년"+today.toString().substring(4,6)+"월"+today.toString().substring(6,8)+"일");
        deadlines.setText(deadline.toString().substring(0,4)+"년"+deadline.toString().substring(4,6)+"월"+deadline.toString().substring(6,8)+"일");
        max_count_text.setText(max_count);
    }
}
