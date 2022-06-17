package com.example.moamoa.ui.chats;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;

    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private ArrayList<ChatsData> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private String NICKNAME;
    private String USERNAME, USERID;
    private String destinationNAME, destinationUID;
    private String FORMID, FirebaseFORMID;
    private String CHATROOM_NAME, CHATROOM_FID;
    ChatModel chatModel;

    private EditText EditText_chat;
    private Button sendbtn;
    //private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference myRef;

    private Toolbar chattoolbar;

    private String nick = "닉네임1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // USERID firebase에서 받아옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        USERID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FINDUserName(USERID);
        Log.e("TEST", "USERNAME: "+USERNAME);
        //USERNAME = NICKNAME;
        Log.e("TEST", "USERID: "+USERID);    // 내 UID

        // ChatsActivity에서 값 받음
        Bundle bundle = getArguments();
        destinationUID = bundle.getString("destinationUID");    // 상대 UID
        FORMID = bundle.getString("FORMID");
        CHATROOM_NAME = bundle.getString("CHATROOM_NAME");

        // 값 잘 받았는지 테스트
        Log.e("TEST", "destinationUID = "+destinationUID);
        Log.e("TEST", "FORMID = "+FORMID);
        Log.e("TEST", "CHATROOM_NAME = "+CHATROOM_NAME);

        //리사이클러뷰와 아답터 정의
        recyclerView = (RecyclerView) root.findViewById(R.id.chats_recyclerview);
        recyclerView.setHasFixedSize(true);
        adapter = new ChatsAdapter(getActivity(), list, nick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        // 채팅방 이름 FormdetailActivity랑 연결시킬 코드((아마도...)
        chattoolbar = (Toolbar) root.findViewById(R.id.toolbar);
        Bundle extra = getArguments();
        if(extra != null){
            CHATROOM_NAME = extra.getString("FormNAME");
            if (CHATROOM_NAME != null){
                chattoolbar.setTitle(CHATROOM_NAME);
            }
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
                ChatsData chats = new ChatsData();

                // ChatsData 데이터설정 (화면용)
                chats.setLeftname(nick);
                chats.setLeftmessage(message);
                chats.setSendedtime(ChatTime());

                // ChatsModel 데이터설정 (DB용)
                ChatModel chatModel = new ChatModel();

                // formID 저장
                chatModel.form_ID = FORMID;
                Log.e("TEST", "form_ID: "+chatModel.form_ID);

                // USERID 저장
                chatModel.users.put(USERID.toString(),true);
                chatModel.users.put(destinationUID.toString(), true);

                Log.e("TEST","CHATROOM_FID: "+CHATROOM_FID);
                if (CHATROOM_FID == null){
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            checkChatRoom();
                        }
                    });
                } else{
                    ChatModel.Comment comments = new ChatModel.Comment();
                    comments.UID = USERID;
                    comments.message = message;

                    Log.e("TEST","comments.uid: "+comments.UID);
                    Log.e("TEST", "comments.message: "+comments.message);

                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(CHATROOM_FID)
                            .child("comments").push().setValue(comments);
                }

                myRef.push().setValue(chats);
                EditText_chat.setText(null);    // edittext 안 내용 삭제
                Log.d(this.getClass().getName(), "메세지 보냄");

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

    void FINDUserName(String UID){
        FirebaseDatabase.getInstance().getReference().child("users").child("nick")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    NICKNAME = item.getKey();
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

    private String ChatTime() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("DefaultLocale")
        String TIME = String.format("%d/%d %d:%d", calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY)-3, calendar.get(Calendar.MINUTE));

        return TIME;
    }

    // 채팅방 중복체크
    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+USERID)
                .equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class); //채팅방 아래 데이터 가져옴
                    // 방 id 가져오기
                    if (chatModel.users.containsKey(destinationUID)){   //destinationUID 있는지 체크
                        CHATROOM_FID = item.getKey();   //방 아이디 가져옴
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}