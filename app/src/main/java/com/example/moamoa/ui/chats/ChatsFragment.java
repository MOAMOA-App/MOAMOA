package com.example.moamoa.ui.chats;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;

    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private ArrayList<ChatsData> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private String nick = "닉네임1";

    private String USERNAME, THEIRNAME;
    private String ChattingRoomID;
    ChatModel chatModel;

    private EditText EditText_chat;
    private Button sendbtn;
    //private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference myRef;

    private Toolbar chattoolbar;
    private String CHATROOM_NAME;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 현재 user 정보 불러오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            USERNAME = user.getDisplayName();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String UID = user.getUid();
        }


        recyclerView = (RecyclerView) root.findViewById(R.id.chats_recyclerview);

        recyclerView.setHasFixedSize(true);
        adapter = new ChatsAdapter(getActivity(), list, nick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        // 채팅방 이름 FormdetailActivity랑 연결시킬 코드((아마도...)
        chattoolbar = (Toolbar) root.findViewById(R.id.toolbar);
        Bundle extra = getArguments();
        if(extra != null){
            CHATROOM_NAME = extra.getString("Subject");
        }
        if (CHATROOM_NAME != null){
            chattoolbar.setTitle(CHATROOM_NAME);
        }


        // 채팅방 열때 필요한거-> 유저 두명 ID, 채팅방 ID
        // chatroom -  채팅방  - user - 유저두명
        //                    - chatmsg


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        EditText_chat = root.findViewById(R.id.EditText_chat);

        sendbtn = (Button) root.findViewById(R.id.Button_send);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = EditText_chat.getText().toString();

                if (message != null) {
                    /*
                    ChatsData chats = new ChatsData(USERNAME, EditText_chat.getText().toString()); // 데이터 묶음
                    myRef.child("message").child(ChattingRoomID).push().setValue(chats); // 데이터 푸쉬
                    EditText_chat.setText(""); // 입력창 초기화

                     */
                    ChatsData chats = new ChatsData();

                    chats.setLeftname(nick);
                    chats.setLeftmessage(message);
                    chats.setSendedtime(ChatTime());

                    myRef.push().setValue(chats);
                    EditText_chat.setText(null);    // edittext 안 내용 삭제
                    Log.d(this.getClass().getName(), "메세지 보냄");
                }

            }
        });


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("메시지 보냄", snapshot.getValue().toString());
                ChatsData chats = snapshot.getValue(ChatsData.class);
                Log.i("채팅 윈도우", "저장된 데이터 : " + chats.getLeftname() + ": " + chats.getLeftmessage());

                adapter.addChat(chats);
                // 스크롤 맨끝으로
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return root;
    }

    private String ChatTime() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("DefaultLocale")
        String TIME = String.format("%d/%d %d:%d", calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY)-3, calendar.get(Calendar.MINUTE));

        return TIME;
    }

}