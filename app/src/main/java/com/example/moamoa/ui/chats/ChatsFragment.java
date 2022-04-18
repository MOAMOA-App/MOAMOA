package com.example.moamoa.ui.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentChatsBinding;
import com.example.moamoa.ui.chatlist.ChatListAdapter;
import com.example.moamoa.ui.chatlist.ChatListData;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {
    private FragmentChatsBinding binding;

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ArrayList<ChatListData> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
        */
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.chats_recyclerview);

        list = ChatListData.createContactsList(10);
        recyclerView.setHasFixedSize(true);
        adapter = new ChatListAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}