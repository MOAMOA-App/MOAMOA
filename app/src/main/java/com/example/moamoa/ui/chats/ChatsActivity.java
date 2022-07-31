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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityChatsBinding;
import com.example.moamoa.databinding.FragmentChatsBinding;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.chatlist.ChatListFragment;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {
    //
    private ActivityChatsBinding binding;

    private List<ChatModel> chatModels = new ArrayList<>();
    private ArrayList<String> destinationUsers = new ArrayList<>();

    String Chatroomname, Formid, destinationuid;
    String UID, myNICK, destinationNICK;

    ChatsFragment chatsFragment = new ChatsFragment();

    private DatabaseReference mDatabase;

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
        Intent getIntent = getIntent();
        destinationuid = getIntent.getStringExtra("destinationUID");

        // USERID 바탕으로 닉네임 찾음
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 내 닉네임 받아옴
                myNICK = dataSnapshot.child("nick").getValue().toString();

                // 받은 값 ChatsFragment에 넘겨줌
                Bundle bundle = new Bundle();
                bundle.putString("CHATROOM_NAME", Chatroomname);
                bundle.putString("FORMID", Formid);
                bundle.putString("destinationUID", destinationuid);
                bundle.putString("destinationNAME", destinationNICK);
                bundle.putString("USERNAME", myNICK);
                chatsFragment.setArguments(bundle);
                /*
                 * ChatsFragment로 값이 안넘어갔던 이유: xml에 fragmentcontainerview 있음
                 * --> 값을 넘기기 전에 ChatsFragment가 만들어져 null값이 됨
                 * 해결 위해 fragmentcontainerview 대신 framelayout 사용 후 밑 코드로 ChatsFragment 연결해줌
                 */


                // 채팅방 이름 세팅
                /*
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

                /*
                TextView chatbar = findViewById(R.id.chatbarname);
                chatbar.setText(destinationNICK);

                 */

                // 프래그먼트 매니저로 chatscontainer에 chatsFragment 연결해줌
                getSupportFragmentManager().beginTransaction().replace(R.id.chatscontainer, chatsFragment).commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        /*
        // 툴바 설정
        Toolbar chatToolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(chatToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_outline_dehaze_24);
        getSupportActionBar().setTitle("");

         */

        FirebaseDatabase.getInstance().getReference().child("users").child(destinationuid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        destinationNICK = snapshot.child("nick").getValue().toString();

                        TextView chatbar = findViewById(R.id.chatbarname);
                        chatbar.setText(destinationNICK);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        // 메뉴 상호작용
        MenuItem select_language = root.findViewById(R.id.select_language);
        MenuItem exitchats = root.findViewById(R.id.exitchats);

        // 채팅창 메뉴
        findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open right drawer
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.openDrawer(Gravity.RIGHT) ;
                    Log.d(this.getClass().getName(), "서랍 열기");

                    /*
                    MenuItem select_language = root.findViewById(R.id.select_language);
                    MenuItem exitchats = root.findViewById(R.id.exitchats);
                    */

                    /*

                    int id = root.getId();

                    if (id == R.id.select_language) {
                        Log.d("TAG", "언어 선택");
                    } else if (id == R.id.exitchats) {
                        Log.d("TAG", "채팅방 나가기");
                    }

                     */

                    /*
                    select_language.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Log.d("TAG", "언어 선택");

                            return false;
                        }
                    });
                    exitchats.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Log.d("TAG", "채팅방 나가기");

                            return false;
                        }
                    });

                     */


                    /*
                    onOptionsItemSelected(MenuItem item){
                        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);

                        int id = root.getId();
                        if (id == R.id.select_language) {
                            toast.setText("언어 선택");
                            Log.d("TAG", "언어 선택");
                        } else if (id == R.id.exitchats) {
                            toast.setText("채팅방 나가기");
                            Log.d("TAG", "채팅방 나가기");
                        }
                    }

                     */

                }
                else{
                    drawer.closeDrawer(Gravity.RIGHT);
                    Log.d(this.getClass().getName(), "서랍 닫기");
                }
            }
        });
        /*


        AlertDialog.Builder exitchats = new AlertDialog.Builder(
                this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        exitchats.setMessage("채팅방을 나가시겠습니까?")
                .setTitle("알림")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Dialog", "채팅방 나가기 취소");
                        Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                    }
                })
                .setNeutralButton("예", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Dialog", "채팅방 나감");
                        Toast.makeText(getApplicationContext(), "채팅방을 나갔습니다.",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();


         */

        // 기본 툴바 숨김
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatdrawer, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);

        int id = root.getId();
        if (id == R.id.select_language) {
            toast.setText("언어 선택");
            Log.d("TAG", "언어 선택");
        } else if (id == R.id.exitchats) {
            toast.setText("채팅방 나가기");
            Log.d("TAG", "채팅방 나가기");
        }


        /*
        switch (item.getItemId()) {
            case R.id.select_language:
                toast.setText("언어 선택");
                Log.d("TAG", "언어 선택");
                return true;
            case R.id.exitchats:
                toast.setText("채팅방 나가기");
                Log.d("TAG", "채팅방 나가기");
                return true;
        }
        toast.show();
         */
        return super.onOptionsItemSelected(item);

    }
}