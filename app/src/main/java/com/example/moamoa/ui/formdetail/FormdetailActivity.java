package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.example.moamoa.ui.chats.ChatsActivity;
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
    String k;
    int count;
    String temp;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formdetail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        temp= intent.getStringExtra("FID");
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
                String category = dataSnapshot.child("category_text").getValue().toString();
                String max_count = dataSnapshot.child("max_count").getValue().toString();
                String today = dataSnapshot.child("today").getValue().toString();
                String deadline = dataSnapshot.child("deadline").getValue().toString();
                num_k= dataSnapshot.child("parti_num").getValue().toString() ;
                image=dataSnapshot.child("image").getValue().toString() ;

                count=Integer.parseInt(dataSnapshot.child("count").getValue().toString());

                Log.d("확인","message상세 이미지 : "+count);
                String UID = dataSnapshot.child("UID_dash").getValue().toString();
                UserFind(UID);
                Initializeform(subject,category,text,cost,num_k+"/"+max_count,today,deadline);
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

                    if (snapshot.getValue()=="host")
                    {
                        //  bb="true";
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {    }
        });

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("form");
                database.child(temp).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // USER 정보 불러옴 (ChatsFragment에서 destinationUID로 사용)
                        String UID = dataSnapshot.child("UID_dash").getValue().toString();

                        // FORM 정보 불러옴(ChatFragment에서 CHATROOM_NAME과 CHATROOM_FID로 사용)
                        String FORMNAME = dataSnapshot.child("subject").getValue().toString();
                        String FID = temp;


                        mDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // USER 닉네임 불러옴 (ChatsFragment에서 destinationNAME으로 사용
                                String USERNAME = dataSnapshot.child("nick").getValue().toString();

                                if (user.getUid().equals(UID)) {
                                    // 본인의 폼에서는 채팅하기 누를 수 없음
                                    Toast.makeText(getApplicationContext(), "내 게시글입니다.", Toast.LENGTH_SHORT).show();
                                } else{
                                    // ChatActivity로 데이터 넘김
                                    Intent intent = new Intent(FormdetailActivity.this, ChatsActivity.class);

                                    // ChatsActivity에 subject, FID, UID 넘겨줌
                                    //intent.putExtra("CHATROOM_NAME", FORMNAME);
                                    //intent.putExtra("CHATROOM_FID", FID);
                                    intent.putExtra("destinationUID", UID);
                                    //intent.putExtra("destinationNAME", USERNAME);

                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                                Log.w("", "loadPost:onCancelled", databaseError.toException());
                                // ...
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
        });


        party_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 



                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (temp.contains(user.getUid())) {
                            Toast.makeText(getApplicationContext(), "호스트입니다", Toast.LENGTH_SHORT).show();
                        }
                        else if(dataSnapshot.child(temp).getValue()==null) {
                            num_b = 1 + Integer.parseInt(num_k);
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child(temp).setValue("parti");
                            FirebaseDatabase.getInstance().getReference("form").child(temp).child("parti_num").setValue(num_b);

                        }
                        else if(dataSnapshot.child(temp).getValue()!=null &&dataSnapshot.child(temp).getValue().equals("parti") )
                        {
                   //         Log.d("MainActivity", "파티 떠라: " + dataSnapshot.child(temp).getKey());
                            Toast.makeText(getApplicationContext(), "참여하였습니다.", Toast.LENGTH_SHORT).show();

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
    @Override
    public void onBackPressed() {
        FirebaseDatabase.getInstance().getReference("form").child(temp).child("count").setValue(count+1);
        finish();
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

    private void Initializeform(String subject,String category,String text,String cost,String max_count,String today,String deadline) {
        TextView subject_text = (TextView) findViewById(R.id.detail_subject);
        TextView category_text = (TextView) findViewById(R.id.detail_category);
        TextView text_text = (TextView) findViewById(R.id.detail_textarea);
        TextView cost_text = (TextView) findViewById(R.id.detail_cost);
        TextView max_count_text = (TextView) findViewById(R.id.detail_party_num);
        TextView start = (TextView) findViewById(R.id.detail_startdate);

        TextView deadlines = (TextView) findViewById(R.id.detail_deadline);

        subject_text.setText(subject);
        category_text.setText(category);
        text_text.setText(text);
        cost_text.setText(cost);
        start.setText(today.toString().substring(0,4)+"년"+today.toString().substring(4,6)+"월"+today.toString().substring(6,8)+"일");
        deadlines.setText(deadline.toString().substring(0,4)+"년"+deadline.toString().substring(4,6)+"월"+deadline.toString().substring(6,8)+"일");
        max_count_text.setText(max_count);
    }

}
