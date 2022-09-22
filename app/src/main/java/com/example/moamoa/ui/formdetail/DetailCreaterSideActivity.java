package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.DecimalFormat;

/**
 * 판매자용 게시글 상세보기 페이지
 */
public class DetailCreaterSideActivity extends Activity {

    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;

    private String image;
    private String temp;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailcreaterside);

        user        = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        temp = intent.getStringExtra("FID");

        //게시글 내용 채우기 함수
        printpage();
        //버튼 선언
        Button notice_btn    = (Button)findViewById(R.id.creator_notice);       //채팅하기 버튼
        Button showparty_btn = (Button)findViewById(R.id.creator_showparty); //참여하기 버튼
        ImageButton menu_btn = (ImageButton)findViewById(R.id.creator_menu); //메뉴보기 버튼

        showparty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.PopupMenu);
                PopupMenu popupMenu = new PopupMenu(wrapper,view);
                popupMenu.inflate(R.menu.detailcreatorside_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case(R.id.Detail_Creator_Change):
                                Log.e("popup","수정하기");
                                Intent intent1 = new Intent(DetailCreaterSideActivity.this, FormChangeActivity.class);
                                intent1.putExtra("FID", intent.getStringExtra("FID"));
                                intent1.putExtra(("UID_dash"),temp);
                                startActivity(intent1);
                                finish();
                                /*
                                    게시글 수정 어디까지 허용할 것인가.
                                 */
                                break;
                            case(R.id.Detail_Creator_Delete):
                                Log.e("popup","삭제하기");
                                AlertDialog.Builder delete_alert = new AlertDialog.Builder(DetailCreaterSideActivity.this, R.style.AlertDialog);

                                delete_alert.setMessage("정말로 삭제하시겠습니까?")
                                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            })
                                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            }).show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.detailcreatorside_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch(item.getItemId()){
            case R.id.Detail_Creator_Change:
                count = 0;
                break;
            case R.id.Detail_Creator_Delete:
                count = 1;
                break;
        }
        return true;
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

                DecimalFormat myFormatter = new DecimalFormat("###,###");
                String cost         = myFormatter.format(dataSnapshot.child("cost").getValue());
                String subject      = dataSnapshot.child("subject").getValue().toString();
                String text         = dataSnapshot.child("text").getValue().toString();
                String category     = dataSnapshot.child("category_text").getValue().toString();
                String max_count    = dataSnapshot.child("max_count").getValue().toString();
                String today        = dataSnapshot.child("today").getValue().toString();
                String deadline     = dataSnapshot.child("deadline").getValue().toString();
                String num_k        = dataSnapshot.child("parti_num").getValue().toString() ;
                String express      = dataSnapshot.child("express").getValue().toString();
                String state        = dataSnapshot.child("state").getValue().toString();

                Resources res = getResources();
                String[] cat = res.getStringArray(R.array.category);
                category=cat[Integer.parseInt(category)];


                image=dataSnapshot.child("image").getValue().toString() ;

                count=Integer.parseInt(dataSnapshot.child("count").getValue().toString());

                Log.d("확인","message상세 이미지 : "+count);
                String UID = dataSnapshot.child("UID_dash").getValue().toString();
                UserFind(UID);
                Initializeform(subject,category,text,cost,num_k+"/"+max_count,today,deadline,express,count,state);
                StorageReference pathReference = firebaseStorage.getReference(image);


                ImageView mainImage   = (ImageView) findViewById(R.id.mainImage);  //상세이미지
                DetailCreaterSideActivity activity = (DetailCreaterSideActivity) mainImage.getContext();
                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (activity.isFinishing()) return;
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
                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (activity1.isFinishing()) return;
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

    private void Initializeform
            (String subject,String category,String text,String cost,String max_count,
             String today,String deadline,String express,Integer count,String state)
    {
        TextView subject_text = (TextView) findViewById(R.id.detail_subject);
        TextView category_text = (TextView) findViewById(R.id.detail_category);
        TextView text_text = (TextView) findViewById(R.id.detail_textarea);
        TextView cost_text = (TextView) findViewById(R.id.detail_cost);
        TextView max_count_text = (TextView) findViewById(R.id.detail_party_num);
        TextView start = (TextView) findViewById(R.id.detail_startdate);
        TextView deadlines = (TextView) findViewById(R.id.detail_deadline);
        TextView express_text = (TextView) findViewById(R.id.detail_express);
        TextView count_text = (TextView) findViewById(R.id.detail_counttext);
        TextView state_text = (TextView) findViewById(R.id.detail_state);

        subject_text.setText(subject);
        category_text.setText(category);
        text_text.setText(text);
        cost_text.setText(cost);
        start.setText(today.toString().substring(0,4)+"년"+today.toString().substring(4,6)+"월"+today.toString().substring(6,8)+"일");
        deadlines.setText(deadline.toString().substring(0,4)+"년"+deadline.toString().substring(4,6)+"월"+deadline.toString().substring(6,8)+"일");
        max_count_text.setText(max_count);
        express_text.setText(express);
        count_text.setText("조회 "+count);

        switch(state){
            case "0":
                state_text.setText("[참여모집]");
                state_text.setTextColor(Color.parseColor("#F1A94E"));
                break;
            case "1":
                state_text.setText("[거래진행]");
                state_text.setTextColor(Color.parseColor("#274E13"));
                break;
            case "2":
                state_text.setText("[거래완료]");
                state_text.setTextColor(Color.parseColor("#4C4C4C"));
                break;
        }
    }
}
