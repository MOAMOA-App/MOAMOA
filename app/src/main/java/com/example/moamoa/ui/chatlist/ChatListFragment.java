package com.example.moamoa.ui.chatlist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatlistBinding;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.chats.ChatModel;
import com.example.moamoa.ui.chats.ChatsActivity;
import com.example.moamoa.ui.chats.ChatsFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ArrayList<ChatListData> list = new ArrayList<>();

    private String USERID, destinationUID;
    private String FORMID;
    private String CHATROOM_NAME, CHATROOM_FID;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd HH:mm");

    private FragmentChatlistBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // USERID firebase에서 받아옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        USERID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = (RecyclerView) root.findViewById(R.id.chatting_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ChatModel> chatModels = new ArrayList<>();
        private String UID;
        private ArrayList<String> destinationUsers = new ArrayList<>();

        public ChatRecyclerViewAdapter(){
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+UID)
                    .equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                // 여기서 equalTo는 true까지의 방만 검색한다. (내가 소속된 방만 띄움)
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // 데이터 받아오기 세팅
                    chatModels.clear();
                    for (DataSnapshot item : snapshot.getChildren()){
                        chatModels.add(item.getValue(ChatModel.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chatlist, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            final CustomViewHolder customViewHolder = (CustomViewHolder) holder;
            String destinationUID = null;
            String formID = null;
            // 채팅방에 있는 유저 체크
            for (String user: chatModels.get(position).users.keySet()){
                if (!user.equals(UID)){ // 있는 유저 중 내가 아닌 사람 뽑아옴
                    destinationUID = user;
                    destinationUsers.add(destinationUID);
                }
            }

            if (destinationUsers != null){
                // destination에 대한 정보 가져오기: users - child(UID) - destinationUID 확인
                String finalDestinationUID = destinationUID;    // makes destinationUID to be final

                // destination이 누군지에 대한 정보 가져오기: users - child(UID) - destinationUID 확인 - formID 가져오기
                // formID 가져오면 Form에서 폼이름과 사진 가져오기
                FirebaseDatabase.getInstance().getReference().child("users").child(finalDestinationUID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                customViewHolder.userName.setText(user.nick);

                                String destinationprofil_text = snapshot.child("image").getValue().toString();
                                FirebaseStorage.getInstance().getReference(destinationprofil_text)
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Activity context = (Activity) customViewHolder.imageView.getContext();
                                        if(context.isFinishing()) return;
                                        Glide.with(customViewHolder.imageView)
                                                .load(uri)
                                                .into(customViewHolder.imageView);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                // 메시지를 내림차순으로 정렬한 뒤 마지막 메시지의 키값을 가져옴
                Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
                commentMap.putAll(chatModels.get(position).comments);
                String lastMessageKey = (String) commentMap.keySet().toArray()[0];
                customViewHolder.recentMessage.setText(chatModels.get(position).comments.get(lastMessageKey).message);
                Log.e("TEST", "lastMessageKey: "+ lastMessageKey);

                //customViewHolder.lastSendtime.setText((Integer) chatModels.get(position).comments.get(lastMessageKey).timestamp);

                Object unixTime = chatModels.get(position).comments.get(lastMessageKey).timestamp;
                long lastMessageTime =  Long.parseLong(unixTime.toString());
                Date date = new Date(lastMessageTime);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                String time = simpleDateFormat.format(date);
                customViewHolder.lastSendtime.setText(time);


                // 누르면 채팅방으로 넘어감(클릭 이벤트)
                customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ChatsActivity.class);
                        intent.putExtra("destinationUID", destinationUsers.get(position));

                        startActivity(intent);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView userName, recentMessage, lastSendtime;

            public CustomViewHolder(View view) {
                super(view);

                imageView = (ImageView) view.findViewById(R.id.chatlist_Image);
                userName = (TextView) view.findViewById(R.id.chat_username);
                recentMessage = (TextView) view.findViewById(R.id.chat_recent);
                lastSendtime = (TextView) view.findViewById(R.id.chat_lastsendtime);

            }
        }
    }
}