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
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.ui.account.User;
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

import java.util.Objects;

public class FormdetailActivity extends Activity {
    private DatabaseReference mDatabase;
    int num_b;
    String num_k;
    String image;
    String k;
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
        //ImageButton heart_btn = (ImageButton) findViewById(R.id.detail_heart_btn);
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
                Initializeform(subject,"category",text,cost,num_k+"/"+max_count);
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



                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("form");
                database.child(temp).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // ChatActivity로 데이터 넘김
                        Intent intent = new Intent(FormdetailActivity.this, ChatsActivity.class);

                        // FORM 정보 불러옴(ChatFragment에서 CHATROOM_NAME과 CHATROOM_FID로 사용)
                        String FORMNAME = dataSnapshot.child("subject").getValue().toString();
                        String FID = temp;

                        // USER 정보 불러옴 (ChatsFragment에서 destinationUID로 사용)
                        String UID = dataSnapshot.child("UID_dash").getValue().toString();

                        // 값 잘 불러왔는지 테스트
                        Log.d("TEST", "subject: "+FORMNAME);
                        Log.d("TEST", "FID: "+FID);
                        Log.d("TEST", "UID: "+UID);

                        // ChatsActivity에 subject, FID, UID 넘겨줌
                        intent.putExtra("CHATROOM_NAME", FORMNAME);
                        intent.putExtra("CHATROOM_FID", FID);
                        intent.putExtra("destinationUID", UID);

                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                });

            }
        });


        party_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 



                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d("MainActivity", "우쉬: " + dataSnapshot.child(temp));

                        Log.d("MainActivity", "파티 폼 : " + dataSnapshot.child(temp).getKey());
                        Log.d("MainActivity", "파티냐 : " + dataSnapshot.child(temp).getValue());
                        if (temp.contains(user.getUid())) {
                            Toast.makeText(getApplicationContext(), "호스트입니다", Toast.LENGTH_SHORT).show();
                        }

                        else if(dataSnapshot.child(temp).getValue()!=null &&dataSnapshot.child(temp).getValue().equals("parti") && num_b!=Integer.parseInt(num_k)+1)
                        {
                            Log.d("MainActivity", "파티 떠라: " + dataSnapshot.child(temp).getKey());
                            Toast.makeText(getApplicationContext(), "이미 참여하였습니다.", Toast.LENGTH_SHORT).show();

                        }
                        else if(dataSnapshot.child(temp).getValue()==null) {
                                num_b = 1 + Integer.parseInt(num_k);
                                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child(temp).setValue("parti");
                                FirebaseDatabase.getInstance().getReference("form").child(temp).child("parti_num").setValue(num_b);

                            }


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {    }
                });

                //파티 하면 숫자 안늘게 하기
            }

        });
        ToggleButton button = findViewById(R.id.heart);
        FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(temp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    k=dataSnapshot.getKey();

                    if (dataSnapshot.getValue().equals("true")) {

                        button.setBackgroundResource(R.drawable.full_heart);

                    } else {
                        button.setBackgroundResource(R.drawable.empty_heart);

                    }
                }

                Log.d("MainActivity", "ValueEventListener : " + dataSnapshot.getKey());

                Log.d("MainActivity", "ValueEventListener : " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {    }
        });

        //FirebaseDatabase.getInstance().getReference("form").child(listViewData.get(position).FID).child("heart_num").setValue(num_a+1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(temp).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //v= String.valueOf(dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {    }
                });


                //Log.d("MainActivity", "vv: " + v);

                if (button.isChecked()) {

                    //button.setBackgroundResource(R.drawable.full_heart);
                    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(temp).setValue("true");
                }
                else if( !button.isChecked())
                {

                    // button.setBackgroundResource(R.drawable.empty_heart);
                    //FirebaseDatabase.getInstance().getReference("form").child(listViewData.get(position).FID).child("heart_num").setValue(num_a-1);
                    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(temp).setValue("false");
                }
                //{
                //    button.setBackgroundResource(R.drawable.full_heart);
                //    FirebaseDatabase.getInstance().getReference("form").child(listViewData.get(position).FID).child("heart_num").setValue(num_a+1);
                //    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).setValue("true");

                //}
                //if (isChecked ){





            }





            //
            //String clickName = listViewData.get(position).subject;
            //Log.d("확인","message : "+clickName);



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
        TextView max_count_text = (TextView) findViewById(R.id.detail_party_num);
        subject_text.setText(subject);
        category_text.setText("1");
        text_text.setText(text);
        cost_text.setText(cost);
        max_count_text.setText(max_count);
    }

}
