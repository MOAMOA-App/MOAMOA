package com.example.moamoa.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private ArrayList<NotificationsData> list = new ArrayList<>();

    private FragmentNotificationsBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        /* 이것도걍남겨둠 아마 뷰모델이랑 연결된거같긴한데...
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        */
        //binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();

        //final TextView textView = binding.textNotifications;
        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //return root;

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.notification_list);

        list = NotificationsData.createContactsList(5); //이것도 폼 등록된 갯수만큼 해주면될듯
        recyclerView.setHasFixedSize(true);
        adapter = new NotificationsAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}