package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.ui.acount.User;
import com.example.moamoa.ui.chats.ChatsActivity;
import com.example.moamoa.ui.chats.ChatsFragment;
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

public class FormdetailActivity extends Activity {
    private DatabaseReference mDatabase;
    int num_b;
    String num_k;
    String image;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formdetail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        String temp = intent.getStringExtra("FID");
        ImageView mainImage = (ImageView) findViewById(R.id.mainImage);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        Button chat_btn = (Button)findViewById(R.id.detail_chat_btn);   //채팅하기 버튼
        Button party_btn = (Button)findViewById(R.id.detail_party_btn); //참여하기 버튼
        ImageButton heart_btn = (ImageButton) findViewById(R.id.detail_heart_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("form");
        mDatabase.child(temp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String subject = dataSnapshot.child("subject").getValue().toString();
                //String category = dataSnapshot.child("category").getValue().toString();
                String text = dataSnapshot.child("text").getValue().toString();
                String cost = dataSnapshot.child("cost").getValue().toString();
                String max_count = dataSnapshot.child("max_count").getValue().toString();
                num_k= dataSnapshot.child("parti_num").getValue().toString() ;
                image=dataSnapshot.child("image").getValue().toString() ;
                Log.d("확인","message상세 이미지 : "+image);
                String UID = dataSnapshot.child("UID_dash").getValue().toString();
                UserFind(UID);
                Initializeform(subject,"category",text,cost,max_count);
                StorageReference pathReference = firebaseStorage.getReference(image);

                FormdetailActivity activity = (FormdetailActivity) mainImage.getContext();
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


        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.d("MainActivity", "ValueEventListener : " + snapshot.getKey());
                    if (snapshot.getValue()=="host")
                    {
                      //  bb="true";
                    }
                    Log.d("MainActivity", "ValueEventListener : " + snapshot.getValue());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {    }
        });
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "채팅하기 클릭", Toast.LENGTH_SHORT).show();

                mDatabase.child("form").child(temp).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Form tochat = snapshot.getValue(Form.class);
                        //String UID = tochat.getUID_dash
                        //String FORMID = tochat.getFID();
                        //String FORMNAME = tochat.getSubject();

                        // ChatsFragment에 FID와 UID 넘겨줌
                        Fragment chats = new ChatsFragment();
                        Bundle bundle = new Bundle();
                        //bundle.putString("FormID", FORMID);
                        //bundle.putString("FormNAME", FORMNAME);
                        //bundle.putString("destinationUID", UID);
                        chats.setArguments(bundle);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(FormdetailActivity.this, ChatsActivity.class);
                startActivity(intent);
            }
        });


        party_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "참여하기 클릭", Toast.LENGTH_SHORT).show();

                if (temp.contains(user.getUid())) {
                    Toast.makeText(getApplicationContext(), "호스트입니다", Toast.LENGTH_SHORT).show();
                }
                else{
                    num_b= 1 + Integer.parseInt(num_k);
                    FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child(temp).setValue("parti");
                    FirebaseDatabase.getInstance().getReference("form").child(temp).child("parti_num").setValue(num_b);

                }
            }
        });
        heart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "찜버튼 클릭", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(temp).setValue("true");            }
        });
        return;
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

                FormdetailActivity activity1 = (FormdetailActivity) profile.getContext() ;
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

    private void Initializeform(String subject,String category,String text,String cost,String max_count) {
        TextView subject_text = (TextView) findViewById(R.id.detail_subject);
        TextView category_text = (TextView) findViewById(R.id.detail_category);
        TextView text_text = (TextView) findViewById(R.id.detail_textarea);
        TextView cost_text = (TextView) findViewById(R.id.detail_cost);
        TextView max_count_text = (TextView) findViewById(R.id.detail_category);
        subject_text.setText(subject);
        category_text.setText("1");
        text_text.setText(text);
        cost_text.setText(cost);
    }

}
