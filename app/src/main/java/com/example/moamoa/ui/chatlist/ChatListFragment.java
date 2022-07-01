package com.example.moamoa.ui.chatlist;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatlistBinding;
import com.example.moamoa.ui.chats.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ArrayList<ChatListData> list = new ArrayList<>();

    private String USERID, destinationUID;
    private String FORMID;
    private String CHATROOM_NAME, CHATROOM_FID;

    private FragmentChatlistBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // USERID firebase에서 받아옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        USERID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        /*
        // ChatsActivity에서 값 받음
        Bundle bundle = getArguments();
        assert bundle != null;
        destinationUID = bundle.getString("destinationUID");    // 상대 UID
        FORMID = bundle.getString("FORMID");
        CHATROOM_NAME = bundle.getString("CHATROOM_NAME");

         */

        this.FormData();

        recyclerView = (RecyclerView) root.findViewById(R.id.chatting_list);
        recyclerView.setHasFixedSize(true);

        /*
        adapter = new ChatListAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

         */

        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        return root;
    }

    public void FormData()
    {
        list = new ArrayList<ChatListData>();
        list.add(new ChatListData("test", "궁금한 점이 있어 문의드립니다!"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ChatModel> chatModels = new ArrayList<>();
        private String UID;

        public ChatRecyclerViewAdapter(){
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+UID).addListenerForSingleValueEvent(new ValueEventListener() {
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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final CustomViewHolder customViewHolder = (CustomViewHolder) holder;
            String destinationUID = null;

            // 채팅방에 있는 유저 체크함
            for (String user: chatModels.get(position).users.keySet()){
                if (!user.equals(UID)){ // 있는 유저 중 내가 아닌 사람 뽑아옴
                    destinationUID = user;
                }
            }
            /*
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

             */
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView formName, recentMessage;

            public CustomViewHolder(View view) {
                super(view);

                imageView = (ImageView) view.findViewById(R.id.chatlist_Image);
                formName = (TextView) view.findViewById(R.id.chat_formname);
                recentMessage = (TextView) view.findViewById(R.id.chat_recent);

            }
        }
    }
}