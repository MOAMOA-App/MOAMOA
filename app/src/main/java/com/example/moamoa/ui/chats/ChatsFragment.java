package com.example.moamoa.ui.chats;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

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

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatsBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        EditText_chat = root.findViewById(R.id.EditText_chat);

        sendbtn = (Button) root.findViewById(R.id.Button_send);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString();
                if (msg != null) {
                    ChatsData chats = new ChatsData();
                    chats.setLeftname(nick);
                    chats.setLeftmessage(msg);

                    long mNow = System.currentTimeMillis();
                    Date mReDate = new Date(mNow);
                    SimpleDateFormat mFormat = new SimpleDateFormat("MM.dd HH:mm");
                    chats.sendedtime = mFormat.format(mReDate);

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
}