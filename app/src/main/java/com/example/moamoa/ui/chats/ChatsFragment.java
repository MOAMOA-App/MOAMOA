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
import com.example.moamoa.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private ArrayList<ChatsData> list = new ArrayList<>();

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_chat);

        list = ChatsData.createContactsList(5); //나오는 갯수는 폼 등록된 갯수만큼 해주면될듯
        recyclerView.setHasFixedSize(true);
        adapter = new ChatsAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;

    }
}