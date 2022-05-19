package com.example.moamoa.ui.chatlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatlistBinding;
import com.example.moamoa.ui.chats.ChatsActivity;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ArrayList<ChatListData> list = new ArrayList<>();

    private FragmentChatlistBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.FormData();


        recyclerView = (RecyclerView) root.findViewById(R.id.chatting_list);

        recyclerView.setHasFixedSize(true);
        adapter = new ChatListAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return root;
    }

    public void FormData()
    {
        list = new ArrayList<ChatListData>();
        list.add(new ChatListData("폼이름1", "안녕하세요"));
        list.add(new ChatListData("폼이름2", "안녕하세요"));
        list.add(new ChatListData("폼이름3", "안녕하세요"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}