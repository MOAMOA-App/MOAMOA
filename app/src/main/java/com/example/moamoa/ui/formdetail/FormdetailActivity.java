package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.example.moamoa.ui.chats.ChatsActivity;
import com.example.moamoa.ui.dashboard.CustomTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FormdetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private DatabaseReference mDatabase;
    String image;
    String k;
    int count;
    String temp;
    String FID;

    int count_party;
    // 지도
    private MapView mapView;
    private static NaverMap naverMap;
    private LatLng myLatLng = new LatLng( 37.3399, 126.733);
    Marker marker = new Marker();
    private Geocoder geocoder;
    String addr_co;

    private RecyclerView mainImage;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        temp = intent.getStringExtra("UID_dash");
        if(user.getUid().equals(temp)){
            Intent intent1 = new Intent(this, DetailCreaterSideActivity.class);
            intent1.putExtra("FID", intent.getStringExtra("FID"));
            intent1.putExtra(("UID_dash"),temp);
            startActivity(intent1);
            finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formdetail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mainImage = (RecyclerView) findViewById(R.id.mainImage);
        firebaseStorage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        FID= intent.getStringExtra("FID");

        Button chat_btn = (Button)findViewById(R.id.detail_chat_btn);   //채팅하기 버튼
        Button party_btn = (Button)findViewById(R.id.detail_party_btn); //참여하기 버튼
        //ImageButton heart_btn = (ImageButton) findViewById(R.id.detail_heart_btn);
        //
        printpage();
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("xjdzzwh9wk"));
        mapView = (MapView)findViewById(R.id.mv);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                temp = intent.getStringExtra("UID_dash");

                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("form");
                database.child(temp).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // USER 정보 불러옴 (ChatsFragment에서 destinationUID로 사용)
                        String UID = temp;
                        // dataSnapshot.child("UID_dash").getValue().toString();

                        // 밑에꺼... 무슨코드인지모르겠음
                        // point=dataSnapshot.child("point").getValue().toString();

                        // FORM 정보 불러옴(ChatFragment에서 CHATROOM_NAME과 CHATROOM_FID로 사용)
                        /*
                        String FORMNAME = dataSnapshot.child("subject").getValue().toString();
                        String FID = temp;
                        */


                        database.child(UID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // USER 닉네임 불러옴 (ChatsFragment에서 destinationNAME으로 사용
                                // String USERNAME = dataSnapshot.child("nick").getValue().toString();

                                if (user.getUid().equals(UID)) {
                                    // 본인의 폼에서는 채팅하기 누를 수 없음
                                    Toast.makeText(getApplicationContext(), "내 게시글입니다.", Toast.LENGTH_SHORT).show();
                                } else{
                                    // ChatActivity로 UID 넘김 (destinationUID)
                                    Intent intent = new Intent(FormdetailActivity.this, ChatsActivity.class);
                                    intent.putExtra("destinationUID", UID);

                                    /*
                                    // ChatsActivity에 subject, FID, UID 넘겨줌
                                    intent.putExtra("CHATROOM_NAME", FORMNAME);
                                    intent.putExtra("CHATROOM_FID", FID);
                                    intent.putExtra("destinationNAME", USERNAME);
                                    */

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


        //참가 버튼
        party_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView state_text = (TextView) findViewById(R.id.detail_state);
                String state = (String) state_text.getText();
                if(state=="[참여모집]"){
                    FirebaseDatabase.getInstance().getReference("party").child(FID).child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(!task.getResult().child(user.getUid()).exists()){
                                FirebaseDatabase.getInstance().getReference("form").child(FID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        int max = Integer.parseInt(String.valueOf(task.getResult().child("max_count").getValue()));
                                        int now = Integer.parseInt(String.valueOf(task.getResult().child("parti_num").getValue()));
                                        if(max>now){
                                            HashMap<String,Object> childUpdates = new HashMap<>();
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("party");
                                            childUpdates.put(user.getUid(),getToday());
                                            reference.child(FID).updateChildren(childUpdates);
                                            FirebaseDatabase.getInstance().getReference("form").child(FID).child("parti_num").setValue(count_party+1);
                                            Toast.makeText(getApplicationContext(), "참여되었습니다", Toast.LENGTH_SHORT).show();
                                            printpage();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "참여 인원이 꽉 찼습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "이미 참여한 게시글 입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "이미 거래가 진행 중인 게시글 입니다.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        ToggleButton button = findViewById(R.id.heart);
        FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(FID).addValueEventListener(new ValueEventListener() {
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(FID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //v= String.valueOf(dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {    }
                });

                if (button.isChecked()) {

                    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(FID).setValue("true");
                }
                else if( !button.isChecked())
                {

                    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(FID).setValue("false");
                }

            }
;
        });
        return;
    }
    @Override
    public void onBackPressed() {
        FirebaseDatabase.getInstance().getReference("form").child(FID).child("count").setValue(count+1);

        Log.d("확인","message상세 이미지 : "+count+1);
        finish();
    }
    private String getToday(){
        SimpleDateFormat mFormat1 = new SimpleDateFormat("yyyyMMddhhmmss");
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);

        return mFormat1.format(mDate);
    }
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        geocoder = new Geocoder(this);
        String[] PointArray=addr_co.split(",");
        Log.d("확인","message상세 이미지 : "+addr_co);
        String latitude = PointArray[0]; // 경도
        String longitude = PointArray[1]; // 경도
        LatLng point1 = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        marker.setPosition(point1);
        marker.setMap(naverMap);
                // 해당 좌표로 화면 줌
        CameraPosition cameraPosition = new CameraPosition(point1, 16);

        naverMap.setCameraPosition(cameraPosition);
    }


    private void printpage(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("form");
        mDatabase.child(FID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String subject = dataSnapshot.child("subject").getValue().toString();
                    String text = dataSnapshot.child("text").getValue().toString();

                    DecimalFormat myFormatter = new DecimalFormat("###,###");
                    String cost         = myFormatter.format(dataSnapshot.child("cost").getValue());
                    String category     = dataSnapshot.child("category_text").getValue().toString();
                    String max_count    = dataSnapshot.child("max_count").getValue().toString();
                    String today        = dataSnapshot.child("today").getValue().toString();
                    String deadline     = dataSnapshot.child("deadline").getValue().toString();
                    String state        = dataSnapshot.child("state").getValue().toString();
                    String express      = dataSnapshot.child("express").getValue().toString();
                    String address   = dataSnapshot.child("address").getValue().toString();
                    String addr_detail   = dataSnapshot.child("addr_detail").getValue().toString();



                    addr_co      = dataSnapshot.child("addr_co").getValue().toString();


                    count_party= Integer.parseInt(dataSnapshot.child("parti_num").getValue().toString()) ;

                    Resources res = getResources();
                    String[] cat = res.getStringArray(R.array.category);
                    category=cat[Integer.parseInt(category)];

                    image=dataSnapshot.child("image").getValue().toString() ;
                    count=Integer.parseInt(dataSnapshot.child("count").getValue().toString());


                    Log.d("확인","message상세 이미지 : "+count);
                    String UID = dataSnapshot.child("UID_dash").getValue().toString();
                    UserFind(UID);
                    Initializeform(subject,category,text,cost,count_party+"/"+max_count,today,deadline,address,addr_detail,express,count,state);
                    StorageReference pathReference = firebaseStorage.getReference(image);


                    FormdetailActivity activity = (FormdetailActivity) mainImage.getContext();

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
             String today,String deadline,String address,String addr_detail, String express,Integer count,String state)
    {
        TextView subject_text   = (TextView) findViewById(R.id.detail_subject);
        TextView category_text  = (TextView) findViewById(R.id.detail_category);
        TextView text_text      = (TextView) findViewById(R.id.detail_textarea);
        TextView cost_text      = (TextView) findViewById(R.id.detail_cost);
        TextView max_count_text = (TextView) findViewById(R.id.detail_party_num);
        TextView start          = (TextView) findViewById(R.id.detail_startdate);
        TextView deadlines      = (TextView) findViewById(R.id.detail_deadline);
        TextView express_text   = (TextView) findViewById(R.id.detail_express);
        TextView count_text     = (TextView) findViewById(R.id.detail_counttext);
        TextView state_text     = (TextView) findViewById(R.id.detail_state);
        TextView address_text       = (TextView) findViewById(R.id.address);
        TextView addr_detail_text    = (TextView) findViewById(R.id.detail_address);

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
        subject_text.setText(subject);
        category_text.setText(category);
        text_text.setText(text);
        cost_text.setText(cost);
        start.setText(today.toString().substring(0,4)+"년"+today.toString().substring(4,6)+"월"+today.toString().substring(6,8)+"일");
        deadlines.setText(deadline.toString().substring(0,4)+"년"+deadline.toString().substring(4,6)+"월"+deadline.toString().substring(6,8)+"일");
        max_count_text.setText(max_count);
        express_text.setText(express);
        count_text.setText("조회 "+count);
        address_text.setText(address);
        addr_detail_text.setText(addr_detail);

    }
}
