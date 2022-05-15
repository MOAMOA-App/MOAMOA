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
    private ChatsAdapter adapter;
    private ArrayList<ChatsData> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.FormData();

        recyclerView = (RecyclerView) root.findViewById(R.id.chats_recyclerview);

        recyclerView.setHasFixedSize(true);
        adapter = new ChatsAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return root;
    }

    public void FormData()
    {
        list = new ArrayList<ChatsData>();
        list.add(new ChatsData("NAME1", "안녕하세요", "NAME2", "안녕하세요"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}