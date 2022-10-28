package com.example.moamoa.ui.formdetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 판매자용 게시글 상세보기 페이지
 */
public class DetailCreaterSideActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    String FID;
    String FUID;
    // 지도
    private MapView mapView;
    private static NaverMap naverMap;
    Marker marker = new Marker();


    ArrayList<PartyListData> partylist=new ArrayList();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailcreaterside);

        firebaseStorage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        FID = intent.getStringExtra("FID");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("form").child(FID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DataSnapshot> task) {
               FUID = task.getResult().child("UID_dash").getValue().toString();
               printpage();

           }
       });
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("xjdzzwh9wk"));
        mapView = (MapView) findViewById(R.id.mv);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        final int[] state = {0};
        TextView state1 = (TextView)findViewById(R.id.formstate_1);
        TextView state2 = (TextView)findViewById(R.id.formstate_2);
        View line1 = (View)findViewById(R.id.formstate_line1);
        View line2 = (View)findViewById(R.id.formstate_line2);
        mDatabase.child("form").child(FID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                state[0] = Integer.parseInt(task.getResult().child("state").getValue().toString());
                if(state[0] >=1) {
                    state1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.second_green)));
                    line1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_orange)));
                    if (state[0] == 2) {
                        state2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_green)));
                        line2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.second_green)));
                    }
                }
            }
        });

        //버튼 선언
        Button notice_btn    = (Button)findViewById(R.id.creator_notice);    //공지하기 버튼
        Button showparty_btn = (Button)findViewById(R.id.creator_showparty); //참여자목록 버튼
        ImageButton menu_btn = (ImageButton)findViewById(R.id.creator_menu); //메뉴보기 버튼

        ArrayList<NoticeData> noticeData = new ArrayList();
        ScrollView scrollView = (ScrollView) DetailCreaterSideActivity.this.findViewById(R.id.decreate_scroll);
        ListView listView = (ListView) DetailCreaterSideActivity.this.findViewById(R.id.Decreator_notice);
        NoticeAdapter myAdapter = new NoticeAdapter(DetailCreaterSideActivity.this,noticeData);
        TextView no = (TextView)  DetailCreaterSideActivity.this.findViewById(R.id.no_notice_text);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

                mDatabase.child("form").child(FID).child("notice").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            noticeData.clear();
                            listView.setVisibility(View.VISIBLE);
                            no.setVisibility(View.GONE);
                            int count = (int) snapshot.getChildrenCount();
                            int x = 0;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                NoticeData listdata = new NoticeData();
                                listdata.setSubject((String) dataSnapshot.child("n_subject").getValue());
                                listdata.setText((String) dataSnapshot.child("n_text").getValue());
                                listdata.setDate((String) dataSnapshot.child("n_date").getValue());
                                noticeData.add(listdata);

                                Log.e("asdf", listdata.getDate() + "");
                                x++;
                                if (x == count) {

                                    listView.setAdapter(myAdapter);
                                }
                            }
                        } else {
                            listView.setVisibility(View.GONE);
                            no.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                CreatorNotice creatorNotice = new CreatorNotice();
                Bundle args = new Bundle();
                args.putString("FID", FID);
                creatorNotice.setArguments(args);
                creatorNotice.show(fragmentManager, "MYTAG");
            }
        });
        showparty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                PartyDialog partyDialog = new PartyDialog();
                Bundle args = new Bundle();
                args.putString("FID", FID);
                partyDialog.setArguments(args);
                partyDialog.show(fragmentManager, "MYTAG");
            }
        });

        //메뉴버튼[수정/삭제]
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
                            case(R.id.Detail_Creator_State):
                                AlertDialog.Builder state_alert = new AlertDialog.Builder(DetailCreaterSideActivity.this);
                                if(state[0]==0){
                                    state_alert.setTitle("거래를 진행하시겠습니까?")
                                        .setMessage("아직 참여인원이 충족되지 않았습니다." +
                                                "거래 진행을 시작하면 더 이상 참가 인원을 받을 수 없습니다.")
                                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                mDatabase.child("form").child(FID).child("state").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        }).show();
                                }else if(state[0]==1){
                                    state_alert.setTitle("거래를 완료하시겠습니까?")
                                            .setMessage("거래 진행을 종료합니다.")
                                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    mDatabase.child("form").child(FID).child("state").setValue(2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            onBackPressed();
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            }).show();
                                }

                                break;
                            case(R.id.Detail_Creator_Change):
                                Intent intent1 = new Intent(DetailCreaterSideActivity.this, FormChangeActivity.class);
                                intent1.putExtra("FID", FID);
                                intent1.putExtra(("UID_dash"),FUID);
                                startActivity(intent1);
                                finish();
                                /*
                                    게시글 수정 어디까지 허용할 것인가.
                                 */
                                break;
                            case(R.id.Detail_Creator_Delete):
                                AlertDialog.Builder delete_alert = new AlertDialog.Builder(DetailCreaterSideActivity.this);
                                delete_alert.setMessage("정말로 삭제하시겠습니까?")
                                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mDatabase.child("form").child(FID).child("active").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    onBackPressed();
                                                }
                                            });
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
    }//onCreate();

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.detailcreatorside_menu,menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        Geocoder geocoder = new Geocoder(this);
        mDatabase.child("form").child(FID).child("addr_co").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.getResult().getValue().toString().length()!=0){
                    String addr_co = task.getResult().getValue().toString();
                    String[] PointArray = addr_co.split(",");
                    String latitude = PointArray[0]; // 경도
                    String longitude = PointArray[1]; // 위도
                    LatLng point1 = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    marker.setPosition(point1);
                    marker.setMap(naverMap);
                    // 해당 좌표로 화면 줌
                    CameraPosition cameraPosition = new CameraPosition(point1, 16);

                    naverMap.setCameraPosition(cameraPosition);
                }else{
                    mapView.setVisibility(View.GONE);
                }

            }
        });
    }
    private void printpage(){
        mDatabase.child("form").child(FID).addValueEventListener(new ValueEventListener() {
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
                    String address      = dataSnapshot.child("address").getValue().toString();
                    String addr_detail  = dataSnapshot.child("addr_detail").getValue().toString();

                    int count_party= Integer.parseInt(dataSnapshot.child("parti_num").getValue().toString()) ;

                    Resources res = getResources();
                    String[] cat = res.getStringArray(R.array.category);
                    category=cat[Integer.parseInt(category)];

                    String image=dataSnapshot.child("image").getValue().toString().replace(".png","_1.png");
                    int count=Integer.parseInt(dataSnapshot.child("count").getValue().toString());

                    UserFind(FUID);
                    String numb ="";
                    if(max_count.equals("1000")){
                        numb="∞";
                    }else{
                        numb = count_party+"/"+max_count;
                    }

                    Initializeform(subject,category,text,cost,numb,today,deadline,address,addr_detail,express,count,state);
                    StorageReference pathReference = firebaseStorage.getReference(image);


                    ImageView mainImage = (ImageView) findViewById(R.id.mainImage);
                    DetailCreaterSideActivity activity = (DetailCreaterSideActivity) mainImage.getContext();
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (activity.isFinishing()) return;
                            Glide.with(mainImage)
                                    .load(uri)
                                    .into(mainImage);
                        }
                    });
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
    private void UserFind(String UID) {
        mDatabase.child("users").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String name;
                String profil_text;
                TextView name_tv = (TextView) findViewById(R.id.detail_nick);
                ImageView profile = (ImageView) findViewById(R.id.detail_profile);

                profil_text = task.getResult().child("image").getValue().toString();
                name = task.getResult().child("nick").getValue().toString();

                TextView local_detail_text   = (TextView) findViewById(R.id.detail_local);

                if(task.getResult().child("area").exists()){
                    String local_detail = task.getResult().child("area").getValue().toString();
                    local_detail_text.setText(local_detail);
                }

                name_tv.setText(name);
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference pathReference = firebaseStorage.getReference(profil_text);

                DetailCreaterSideActivity activity1 = (DetailCreaterSideActivity) profile.getContext();
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
        });
    }

    private void Initializeform
            (String subject,String category,String text,String cost,String numb,
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
        TextView addr_detail_text   = (TextView) findViewById(R.id.detail_address);

        if(numb.equals("∞")){
            numb="인원제한없음";
            max_count_text.setTextSize(15);
        }
        subject_text.setText(subject);
        category_text.setText(category);
        text_text.setText(text);
        cost_text.setText(cost);
        start.setText(today.substring(2,4)+"년 "+today.substring(4,6)+"월 "+today.substring(6,8)+"일");
        deadlines.setText(deadline.substring(2,4)+"년 "+deadline.substring(4,6)+"월 "+deadline.substring(6,8)+"일");
        max_count_text.setText(numb);
        express_text.setText(express);
        count_text.setText("조회 "+count);
        address_text.setText(address);
        addr_detail_text.setText(addr_detail);

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
