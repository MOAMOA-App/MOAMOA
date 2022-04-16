package com.example.moamoa.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
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
import com.example.moamoa.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private DashboardAdapter adapter;
    private ArrayList<DashboardData> list = new ArrayList<>();

    private FragmentDashboardBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //이거는... 뭔코드인지 모르는데 혹시해서 냄겨둠
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //화면 연결
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.chatting_list);

        list = DashboardData.createContactsList(5); //나오는 갯수는 폼 등록된 갯수만큼 해주면될듯
        recyclerView.setHasFixedSize(true);
        adapter = new DashboardAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;
        // return root
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}