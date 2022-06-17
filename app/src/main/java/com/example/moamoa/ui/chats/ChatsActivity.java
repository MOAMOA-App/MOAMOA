package com.example.moamoa.ui.chats;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityChatsBinding;
import com.example.moamoa.databinding.FragmentChatsBinding;
import com.google.android.material.navigation.NavigationView;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;

    String Chatroomname, Formid, destinationuid;
    ChatsFragment chatsFragment = new ChatsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        // FormdetailActivity에서 값 받음
        Intent getIntent = getIntent();
        Chatroomname = getIntent.getStringExtra("CHATROOM_NAME");
        Formid = getIntent.getStringExtra("FORMID");
        destinationuid = getIntent.getStringExtra("destinationUID");

        Log.e("TEST", "Chatroomname "+Chatroomname);
        Log.e("TEST", "Formid "+Formid);
        Log.e("TEST", "destinationuid "+destinationuid);

        // 받은 값 ChatsFragment에 넘겨줌
        // 하...왜일을두번하지 나도일을두번하기싫었는데 고치기엔이미숨을안쉬는코드임
        // ㅁㅊ 일을두번해도숨을안쉬는데요


        Bundle bundle = new Bundle();

        bundle.putString("CHATROOM_NAME", Chatroomname);
        bundle.putString("FORMID", Formid);
        bundle.putString("destinationUID", destinationuid);
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        chatsFragment.setArguments(bundle);
        //ahfahefawfawfㅁㅇㄴㄻㅇㄴㄹㄴㅇㅁㄹ하.......또왜안되는데

        /*프래그먼트 매니저가 프래그먼트를 담당한다!*/
        getSupportFragmentManager().beginTransaction().replace(R.id.chatscontainer, chatsFragment).commit();

        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        //

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