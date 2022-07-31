package com.example.moamoa.ui.chats;

import android.annotation.SuppressLint;
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
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatsBinding;
import com.example.moamoa.ui.account.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;

    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private ArrayList<ChatsData> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private String USERNAME, USERID;
    private String destinationNAME, destinationUID;
    private String FORMID;
    String CHATROOM_NAME, CHATROOM_FID;

    private EditText EditText_chat;
    private Button sendbtn;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd HH:mm");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // USERID firebase에서 받아옴
        USERID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // ChatsActivity에서 destinationUID 받아옴
        Bundle bundle = getArguments();
        assert bundle != null;
        destinationUID = bundle.getString("destinationUID");    // 상대 UID

        //리사이클러뷰와 아답터 정의
        recyclerView = (RecyclerView) root.findViewById(R.id.chats_recyclerview);
        recyclerView.setHasFixedSize(true);

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

                // 방 중복 방지
                if (CHATROOM_FID == null){
                    sendbtn.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms")
                            .push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            checkChatRoom();
                        }
                    });

                } else{
                    ChatModel.Comment comments = new ChatModel.Comment();
                    comments.UID = USERID;
                    comments.message = EditText_chat.getText().toString();
                    comments.timestamp = ServerValue.TIMESTAMP;

                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(CHATROOM_FID)
                            .child("comments").push().setValue(comments);
                }

                EditText_chat.setText(null);    // edittext 안 내용 삭제
                Log.d(this.getClass().getName(), "메세지 보냄");

            }
        });

        return root;
    }

    // 채팅 보낸 시간 불러옴
    private String ChatTime() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("DefaultLocale")
        String TIME = String.format("%d/%d %d:%d", calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

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
                        sendbtn.setEnabled(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUID).addListenerForSingleValueEvent(new ValueEventListener() {
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
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(CHATROOM_FID).child("comments").addValueEventListener(new ValueEventListener() {
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

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = (MessageViewHolder)holder;

            if (comments.get(position).UID.equals(USERID)){
                // 내가 보낸 메시지일시 오른쪽에서 출력:왼쪽 이미지
                //messageViewHolder.nickName.setVisibility(View.INVISIBLE);
                messageViewHolder.nickName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                messageViewHolder.nickName.setText(USERNAME);
                messageViewHolder.Message.setText(comments.get(position).message);
                messageViewHolder.profile_image.setVisibility(View.INVISIBLE); //프사 안보이게

                messageViewHolder.chatLayout.setGravity(Gravity.END);
                messageViewHolder.LinearChatMsg.setGravity(Gravity.END);


            } else {
                Glide.with(holder.itemView.getContext())
                        .load(user.profile_img)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.profile_image); // 상대방의 프사 설정

                messageViewHolder.nickName.setText(user.nick);  // 닉네임 설정
                messageViewHolder.nickName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                messageViewHolder.Message.setText(comments.get(position).message);
                messageViewHolder.profile_image.setVisibility(View.VISIBLE); //프사 보이게

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