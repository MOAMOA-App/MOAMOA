package com.example.moamoa.ui.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityChatsBinding;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.chatlist.ChatListFragment;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.example.moamoa.ui.home.homelist_adapter;
import com.example.moamoa.ui.home.homelist_data;
import com.example.moamoa.ui.notifications.NotificationsAdapter;
import com.example.moamoa.ui.notifications.NotificationsData;
import com.example.moamoa.ui.search.SearchActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsActivity extends AppCompatActivity {
    //
    private ActivityChatsBinding binding;

    private List<ChatModel> chatModels = new ArrayList<>();
    private ArrayList<String> destinationUsers = new ArrayList<>();

    private String FID;
    String CHATROOM_FID;

    private ChatsFragment chatsFragment = new ChatsFragment();

    private DatabaseReference mDatabase;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private String UID, destinationuid;
    TextView TextView_mynick, TextView_mynation;
    TextView chatbar, TextView_destinationnick, TextView_destinationsnation;
    TextView TextView_currentlang;
    ImageView myPfImage, destinationPfImage;

    LinearLayout linearLayout_form;
    TextView TextView_formname, TextView_nickname;

    String userlang = null;

    private final HashMap<Integer, String> langHashmap = new HashMap<>();
    static int select_lang = 0;

    final int DIALOG_EXITROOM = 1;
    final int DIALOG_SELECTLANG = 2;

    private static String formuserid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        // 사용자의 UID, 닉네임 불러옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // FormdetailActivity와 ChatListFragment에서 값 받음
        // ChatsFragment에서 FID
        Intent getIntent = getIntent();
        destinationuid = getIntent.getStringExtra("destinationUID");
        FID = getIntent.getStringExtra("FID");

        if (getIntent.getStringExtra("CHATROOM_FID") != null){
            CHATROOM_FID = getIntent.getStringExtra("CHATROOM_FID");
        }

        // 받은 값 ChatsFragment에 넘겨줌
        Bundle bundle = new Bundle();
        bundle.putString("destinationUID", destinationuid);
        if (FID!=null)
            bundle.putString("FID", FID);
        chatsFragment.setArguments(bundle);
        /*
         * ChatsFragment로 값이 안넘어갔던 이유: xml에 fragmentcontainerview 있음
         * --> 값을 넘기기 전에 ChatsFragment가 만들어져 null값이 됨
         * 해결 위해 fragmentcontainerview 대신 framelayout 사용 후 밑 코드로 ChatsFragment 연결해줌
         */

        // 프래그먼트 매니저로 chatscontainer에 chatsFragment 연결해줌
        getSupportFragmentManager().beginTransaction().replace(R.id.chatscontainer, chatsFragment).commit();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase = FirebaseDatabase.getInstance();
        //recyclerView = findViewById(R.id.chats_recyclerview_userinfo);

        //getUserList();

        // 해시맵에 사용언어 설정을 위한 string값 넣어줌
        langHashmap.put(0, "KOR");
        langHashmap.put(1, "ENG");
        langHashmap.put(2, "CHI(CN)");
        langHashmap.put(3, "CHI(TW)");

        // 채팅방 이름 설정
        chatbar = findViewById(R.id.chatbarname);

        // 내 프로필
        TextView_mynick = (TextView) findViewById(R.id.chats_TextView_mynickname);
        TextView_mynation = (TextView) findViewById(R.id.chats_mynationality);
        myPfImage = (ImageView) findViewById(R.id.chats_myprofile_image);
        getuserprofile(UID);

        // 상대방 프로필
        TextView_destinationnick = (TextView) findViewById(R.id.chats_TextView_theirnickname);
        TextView_destinationsnation = (TextView) findViewById(R.id.chats_theirnationality);
        destinationPfImage = (ImageView) findViewById(R.id.chats_theirprofile_image);
        getuserprofile(destinationuid);

        // 현재 언어. 유저 정보 없을 시 한국어, 있을 시 해당 언어로 나오게
        final String[] language = getResources().getStringArray(R.array.selectlanguage);
        TextView_currentlang = (TextView) findViewById(R.id.chats_TextView_currentlang);
        mDatabase.child("users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("language")){
                    String userlang = snapshot.child("language").getValue().toString();
                    for(Map.Entry<Integer, String> entry : langHashmap.entrySet()){
                        // 동일한 값 있으면 반복문 종료
                        if(entry.getValue().equals(userlang)) { // 값이 null이면 NullPointerException 예외 발생
                            Integer key = entry.getKey();
                            TextView_currentlang.setText(language[key]);
                            break;
                        }
                    }
                } else
                    TextView_currentlang.setText("한국어");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 관련 폼 목록
        linearLayout_form = (LinearLayout) findViewById(R.id.chats_LL_form);
        TextView_formname = (TextView) findViewById(R.id.chat_formlist_formname1);
        TextView_nickname = (TextView) findViewById(R.id.chat_formlist_usernick1);
        checkRoomID();  // FID 불러옴
        setforminfo();
        getformuserid(FID);
        linearLayout_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티 이동 + 값 전달
                Intent intent = new Intent(ChatsActivity.this, FormdetailActivity.class);
                intent.putExtra("FID", FID);

                intent.putExtra("UID_dash", formuserid);
                startActivity(intent);
            }
        });

        /* 채팅방 이름 세팅
         * 그러니까 여기서 뭘해야되냐면... 일단 거기서도 채팅이 되고 채팅 리스트가 따로 있는 한 넘겨받아서 할순없음
         * 왜냐면 넘겨받은값은 폼에서 들어갈때는 몰라도 채팅 리스트에선 안넘겨받앗으니까 없는거잖아...
         * 그럼 거기 있는게 뭐냐면 일단 사람 아이디는 있는듯
         * 습 그럼 그냥 폼으로 나누겠다는 걸 없애고 그냥 사람이랑 채팅을 하고
         *
         * 정리
         * 공지--> 폼이 중요 (추후 제작) 폼 정보 넣어서 공지방 만듦
         * 그 외 문의채팅--> 폼 중요X 사람 정보만 있으면 됨
         * 그럼 폼 제목에 사람 이름 넣는걸로 하면 될듯 굿굿
         * */

        // 채팅창 메뉴
        findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open right drawer
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.openDrawer(Gravity.RIGHT) ;
                    Log.d(this.getClass().getName(), "서랍 열기");
                }
                else{
                    drawer.closeDrawer(Gravity.RIGHT);
                    Log.d(this.getClass().getName(), "서랍 닫기");
                }
            }
        });

        findViewById(R.id.chats_btn_getoutofroom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_EXITROOM); // 다이얼로그 호출
            }
        });

        findViewById(R.id.chats_btn_selectlanguage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_SELECTLANG); // 다이얼로그 호출
            }
        });

        // 기본 툴바 숨김
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void getformuserid(String FID){
        mDatabase.child("form").child(FID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Form form = snapshot.getValue(Form.class);
                assert form != null;
                formuserid = form.UID_dash;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setforminfo() {
        mDatabase.child("form").child(FID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Form form = snapshot.getValue(Form.class);
                assert form != null;
                TextView_formname.setText(form.subject);

                mDatabase.child("users").child(form.UID_dash).addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        TextView_nickname.setText(user.nick);

                        mDatabase.child("users").child(destinationuid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                User user1 = datasnapshot.getValue(User.class);
                                assert user1 != null;
                                chatbar.setText(user1.nick+"  -  "+form.subject);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * 채팅방 아이디 / FID 가져옴
     */
    void checkRoomID(){
        mDatabase.child("chatrooms").orderByChild("users/"+UID).equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()){
                            ChatModel chatModel = item.getValue(ChatModel.class); //채팅방 아래 데이터 가져옴
                            // 방 id 가져오기
                            if (chatModel.users.containsKey(destinationuid)){   //destinationUID 있는지 체크
                                CHATROOM_FID = item.getKey();   //방 아이디 가져옴

                                mDatabase.child("chatrooms").child(CHATROOM_FID).child("fids")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                FID = snapshot.getValue().toString();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    /**
     * 메뉴에서 유저 정보 설정
     * @param string UID 정보
     */
    private void getuserprofile(String string){
        mDatabase.child("users").child(string).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (string.equals(UID)){
                    // 헤더에서 내 닉네임 보여줌
                    TextView_mynick.setText(user.nick);
                    //TextView_mynation.setText(getUserLangInfo(string));
                    getUserLangInfo(string);

                    // 헤더에서 내 프사 보여줌
                    String myprofil_text = snapshot.child("image").getValue().toString();
                    FirebaseStorage.getInstance().getReference(myprofil_text)
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Activity context = (Activity) myPfImage.getContext();
                                    if(context.isFinishing()) return;
                                    Glide.with(myPfImage)
                                            .load(uri)
                                            .into(myPfImage);
                                }
                            });
                } else {
                    // 채팅방 이름 설정
                    chatbar.setText(user.nick);

                    // 헤더에서 상대방 닉네임 보여줌
                    TextView_destinationnick.setText(user.nick);
                    getUserLangInfo(string);

                    // 헤더에서 상대방 프사 보여줌
                    String destinationprofil_text = snapshot.child("image").getValue().toString();
                    FirebaseStorage.getInstance().getReference(destinationprofil_text)
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Activity context = (Activity) destinationPfImage.getContext();
                                    if(context.isFinishing()) return;
                                    Glide.with(destinationPfImage)
                                            .load(uri)
                                            .into(destinationPfImage);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * 유저 사용언어 정보 받아옴, 없을 시 기본값 설정(한국어)
     * @param string UID
     * @return string userlang 유저 언어 정보
     */
    private void getUserLangInfo(String string){
        mDatabase.child("users").child(string).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("language")){
                    String userlang = snapshot.child("language").getValue().toString();
                    if (string.equals(UID))
                        TextView_mynation.setText(userlang);
                    else
                        TextView_destinationsnation.setText(userlang);
                }else{
                    mDatabase.child("users").child(string).child("language").setValue("KOR");

                    if (string.equals(UID))
                        TextView_mynation.setText(userlang);
                    else
                        TextView_destinationsnation.setText(userlang);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected Dialog onCreateDialog(int id){
        Resources res = getResources();
        switch (id){
            case DIALOG_EXITROOM:
                androidx.appcompat.app.AlertDialog.Builder builder1
                        = new androidx.appcompat.app.AlertDialog.Builder(ChatsActivity.this);

                builder1.setTitle("채팅방 나가기")
                        .setMessage("채팅방을 나가시겠습니까?");

                builder1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder1.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                return builder1.create();

            case DIALOG_SELECTLANG:
                final String[] lang = res.getStringArray(R.array.selectlanguage);
                androidx.appcompat.app.AlertDialog.Builder builder2
                        = new androidx.appcompat.app.AlertDialog.Builder(ChatsActivity.this);

                builder2.setTitle("언어 선택");

                builder2.setSingleChoiceItems(lang, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ChatsActivity.this, lang[which], Toast.LENGTH_SHORT).show();
                        select_lang = Arrays.asList(lang).indexOf(lang[which]);

                        TextView_mynation.setText(langHashmap.get(select_lang));
                        TextView_currentlang.setText(lang[which]);

                        // 선택한 언어 정보 파이어베이스에 업뎃함
                        mDatabase.child("users").child(UID).child("language").setValue(langHashmap.get(select_lang));

                        // 바뀐 정보 프래그먼트에 전달해줌 (settingLangInfo: ChatFragment의 유저 언어코드 설정 함수)
                        chatsFragment.settingLangInfo(UID);

                        dialog.dismiss(); // 누르면 바로 닫히는 형태
                    }
                });

                return builder2.create();
        }
        return super.onCreateDialog(id);
    }
}