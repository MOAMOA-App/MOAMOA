package com.example.moamoa.ui.chats;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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

import com.example.moamoa.ChatModel;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;

    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private ArrayList<ChatsData> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private String nick = "닉네임1";
    private String destinationUID;

    private String UID;
    private String chatRoomUID;

    private EditText EditText_chat;
    private Button sendbtn;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // this.AddData();

        recyclerView = (RecyclerView) root.findViewById(R.id.chats_recyclerview);

        recyclerView.setHasFixedSize(true);
        adapter = new ChatsAdapter(getActivity(), list, nick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");



        // 단말기에 로그인된 UID
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Activity의 getintent().getStringExtra 대체. destinationUID 값 받아옴
        Bundle extra = getArguments();
        if(extra != null){
            destinationUID= extra.getString("destinationUID");
        }

        EditText_chat = root.findViewById(R.id.EditText_chat);

        long mNow = System.currentTimeMillis();
        Date mReDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("MM.dd HH:mm");

        sendbtn = (Button) root.findViewById(R.id.Button_send);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메세지를 보낼 때 방을 새로 만든다.
                ChatModel chatModel = new ChatModel();

                // 현재 대화하는 사람들 넣어주기
                chatModel.users.put(UID, true);
                chatModel.users.put(UID, true);


                if (chatRoomUID == null){
                    sendbtn.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms")
                            .push() // push 안넣어주면 채팅방 이름이 없음. push 만들어줘야 이름 인위적으로 생성됨
                            .setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // 콜백 달아줌. 데이터 완료되었을때 체크. (채팅방이 이미 있는지)
                            checkChatRoom();
                            Log.d("TAG", "checkChatRoom 실행");
                        }
                    });
                    // 여기에 checkChatRoom 있어도 실행은 되나 인터넷이 끊겼다가 복구되었을 때 위에는 씹혀서
                    // 데이터도 없는데 check만 될수도. (오류방지)
                    // 근데...... 제대로작동이안됨... ... ... ... .......

                }
                else{

                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.UID = UID;
                    comment.message = EditText_chat.getText().toString();

                    if (comment.message != null) {
                        ChatsData chats = new ChatsData();
                        chats.setLeftname(nick);
                        chats.setLeftmessage(comment.message);
                        chats.sendedtime = ChatTime();

                        myRef.push().setValue(chats);
                        EditText_chat.setText(null);    // edittext 안 내용 삭제
                        Log.d(this.getClass().getName(), "메세지 보냄");
                    }

                    FirebaseDatabase.getInstance().getReference().child("chatrooms")
                            .child(chatRoomUID).child("comments").push().setValue(comment);
                }


                /*
                if (message != null) {
                    ChatsData chats = new ChatsData();
                    chats.setLeftname(nick);
                    chats.setLeftmessage(message);

                    long mNow = System.currentTimeMillis();
                    Date mReDate = new Date(mNow);
                    SimpleDateFormat mFormat = new SimpleDateFormat("MM.dd HH:mm");
                    chats.sendedtime = mFormat.format(mReDate);

                    myRef.push().setValue(chats);
                    EditText_chat.setText(null);    // edittext 안 내용 삭제
                    Log.d(this.getClass().getName(), "메세지 보냄");
                }

                 */

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

    private String ChatTime(){
        long mNow = System.currentTimeMillis();
        Date mReDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("MM.dd HH:mm");
        String formatDate = mFormat.format(mReDate);

        return formatDate;
    }

    void checkChatRoom(){
        // chatrooms 중복체크
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+UID)
                .equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class);

                    // database의 chatrooms-user에서 해당 아이디 있는지 check
                    if (chatModel.users.containsKey(destinationUID))
                        chatRoomUID = item.getKey();
                        sendbtn.setEnabled(true);
                        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        //recyclerView.setAdapter(new ChatsAdapter());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}