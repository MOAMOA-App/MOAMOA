package com.example.moamoa.ui.chats;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatsBinding;
import com.example.moamoa.ui.account.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;

    LinearLayoutManager linearLayoutManager;

    DatabaseReference mdatabase;

    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private ArrayList<ChatsData> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private String USERNAME, USERID;
    private String destinationNAME, destinationUID;
    private String FORMID;
    String CHATROOM_FID;

    private EditText EditText_chat;
    private Button sendbtn;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd HH:mm");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mdatabase = FirebaseDatabase.getInstance().getReference();

        // USERID firebase에서 받아옴
        USERID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // ChatsActivity와 FormdetailActivity에서 destinationUID 받아옴
        Bundle bundle = getArguments();
        assert bundle != null;
        destinationUID = bundle.getString("destinationUID");    // 상대 UID
        if (bundle.getString("FID") != null)
            FORMID = bundle.getString("FID");
        Log.e("TEST444", "FORMID: "+FORMID);


        //리사이클러뷰 정의
        recyclerView = (RecyclerView) root.findViewById(R.id.chats_recyclerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);

        // 전송버튼 눌렀을 때의 동작
        sendbtn = (Button) root.findViewById(R.id.Button_send);
        EditText_chat = (EditText) root.findViewById(R.id.EditText_chat);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // USERID 저장
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(USERID.toString(),true);
                chatModel.users.put(destinationUID.toString(), true);

                ChatModel.Comment comments = new ChatModel.Comment();
                comments.UID = USERID;
                comments.message = EditText_chat.getText().toString();
                comments.timestamp = ServerValue.TIMESTAMP;

                // 만약 방 아래 같은 FID가 존재하지 않는다면 put
                if (FORMID!=null)
                {
                    Log.e("TEST444", "FORMID: "+FORMID);
                    if (!chatModel.fids.equals(FORMID))
                        chatModel.fids = FORMID;
                    //.put(FORMID.toString());
                }


                // 방 중복 방지
                if (CHATROOM_FID == null){
                    if (!comments.message.equals("")){
                        sendbtn.setEnabled(false);
                        mdatabase.child("chatrooms").push().setValue(chatModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        mdatabase.child("chatrooms").orderByChild("users/"+USERID).equalTo(true)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot item : snapshot.getChildren()){
                                                            ChatModel chatModel = item.getValue(ChatModel.class); //채팅방 아래 데이터 가져옴
                                                            // 방 id 가져오기
                                                            if (chatModel.users.containsKey(destinationUID)){   //destinationUID 있는지 체크
                                                                CHATROOM_FID = item.getKey();   //방 아이디 가져옴
                                                                sendbtn.setEnabled(true);

                                                                Intent intent = new Intent(getContext(), ChatsActivity.class);
                                                                intent.putExtra("destinationUID", destinationUID);
                                                                intent.putExtra("FID", FORMID);
                                                                intent.putExtra("CHATROOM_FID", CHATROOM_FID);

                                                                getActivity().finish();
                                                                startActivity(intent);

                                                                recyclerView.setLayoutManager(linearLayoutManager);
                                                                recyclerView.setAdapter(new RecyclerViewAdapter());
                                                                mdatabase.child("chatrooms").child(CHATROOM_FID).child("comments")
                                                                        .push().setValue(comments);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                    }
                                });
                    }


                } else{
                    if (!comments.message.equals("")){
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                        FirebaseDatabase.getInstance().getReference().child("chatrooms")
                                .child(CHATROOM_FID).child("comments")
                                .push().setValue(comments);
                    }

                }
                EditText_chat.setText(null);    // edittext 안 내용 삭제
                Log.d(this.getClass().getName(), "메세지 보냄");

            }
        });
        checkChatRoom();

        return root;
    }

    // 채팅방 중복체크
    void checkChatRoom(){
        mdatabase.child("chatrooms").orderByChild("users/"+USERID).equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()){
                            ChatModel chatModel = item.getValue(ChatModel.class); //채팅방 아래 데이터 가져옴
                            // 방 id 가져오기
                            if (chatModel.users.containsKey(destinationUID)){   //destinationUID 있는지 체크
                                CHATROOM_FID = item.getKey();   //방 아이디 가져옴

                                //String formid = item.child("formid").getValue().toString();

                                sendbtn.setEnabled(true);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(new RecyclerViewAdapter());


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // 여기서부터 어댑터
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<ChatModel.Comment> comments;
        User user;

        public RecyclerViewAdapter(){
            comments = new ArrayList<>();

            mdatabase.child("users").child(destinationUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    getMessageList();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        void getMessageList() {
            mdatabase.child("chatrooms").child(CHATROOM_FID).child("comments").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();
                    for(DataSnapshot item : snapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    notifyDataSetChanged(); // 리스트 갱신
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_chats, parent, false);

            return new MessageViewHolder(view);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = (MessageViewHolder)holder;

            if (comments.get(position).UID.equals(USERID)){
                // 내가 보낸 메시지일시 오른쪽에서 출력:왼쪽 이미지
                //messageViewHolder.nickName.setVisibility(View.INVISIBLE);
                messageViewHolder.nickName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                mdatabase.child("users").child(USERID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        USERNAME = snapshot.child("nick").getValue().toString();
                        messageViewHolder.nickName.setText(USERNAME);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                messageViewHolder.Message.setText(comments.get(position).message);
                //messageViewHolder.Message.setBackground(requireContext().getResources()
                //        .getDrawable(R.drawable.speechbubbletest));
                messageViewHolder.profile_image.setVisibility(View.INVISIBLE); //프사 안보이게

                messageViewHolder.chatLayout.setGravity(Gravity.END);
                messageViewHolder.LinearChatMsg.setGravity(Gravity.END);
                messageViewHolder.cv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                messageViewHolder.chatLayout.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                messageViewHolder.LinearChatMsg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                messageViewHolder.cv.setForegroundGravity(Gravity.END);

            } else {
                // 상대방의 프사 설정
                FirebaseDatabase.getInstance().getReference().child("users").child(destinationUID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String destinationprofil_text = snapshot.child("image").getValue().toString();

                                Activity context2 = (Activity) messageViewHolder.profile_image.getContext();
                                FirebaseStorage.getInstance().getReference(destinationprofil_text)
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                if (context2.isFinishing()) return;
                                                Glide.with(messageViewHolder.profile_image)
                                                        .load(uri)
                                                        .into(messageViewHolder.profile_image);
                                            }
                                        });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                messageViewHolder.nickName.setText(user.nick);  // 닉네임 설정
                messageViewHolder.nickName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                //messageViewHolder.Message.setBackground(requireContext().getResources()
                //        .getDrawable(R.drawable.speechbubbletest));
                messageViewHolder.Message.setText(comments.get(position).message);
                messageViewHolder.profile_image.setVisibility(View.VISIBLE); //프사 보이게
                messageViewHolder.nickName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                messageViewHolder.cv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                messageViewHolder.cv.setForegroundGravity(Gravity.START);
                messageViewHolder.chatLayout.setGravity(Gravity.START);
            }

            ((MessageViewHolder)holder).Message.setText(comments.get(position).message);

            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.sendedTime.setText(time);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder{
            public TextView nickName, Message, sendedTime;
            ImageView profile_image;
            CardView cv;

            LinearLayout chatLayout, LinearChatMsg;

            public MessageViewHolder(View view){
                super(view);
                profile_image = (ImageView) view.findViewById(R.id.profile_image);
                nickName = (TextView) view.findViewById(R.id.chat_nickname);
                Message = (TextView) view.findViewById(R.id.chat_msg);
                cv = (CardView) view.findViewById(R.id.chat_cardview);
                sendedTime = (TextView) view.findViewById(R.id.sended_time);

                chatLayout = (LinearLayout) view.findViewById(R.id.chatting_layout);
                LinearChatMsg = (LinearLayout) view.findViewById(R.id.Linear_chatmsg);
            }
        }
    }
}