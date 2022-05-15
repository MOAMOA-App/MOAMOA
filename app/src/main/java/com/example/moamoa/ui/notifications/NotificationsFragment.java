package com.example.moamoa.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentNotificationsBinding;
import com.example.moamoa.ui.chats.ChatsData;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private ArrayList<NotificationsData> list = new ArrayList<>();

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.FormData();
        //ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.notification_list);

        recyclerView.setHasFixedSize(true);
        adapter = new NotificationsAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return root;

    }

    public void FormData()
    {
        list = new ArrayList<NotificationsData>();
        list.add(new NotificationsData("참여한 폼에 변동 사항이 있습니다.", "폼이름1"));
        list.add(new NotificationsData("참여한 폼에 변동 사항이 있습니다.", "폼이름2"));
        list.add(new NotificationsData("참여한 폼에 변동 사항이 있습니다.", "폼이름3"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}