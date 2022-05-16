package com.example.moamoa.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.databinding.FragmentHomeBinding;
import com.example.moamoa.R;
import com.example.moamoa.ui.formdetail.show_detail;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ArrayList<homelist_data> homelist;
    private homelist_adapter homelistAdapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homelist = new ArrayList<>();
        InitializeFormData("", "제목1","이름1","9/10","0");
        InitializeFormData("", "제목2","이름2","2/4","1");
        InitializeFormData("", "제목3","이름3","13/15","2");
        InitializeFormData("", "제목4","이름4","13/15","2");
        InitializeFormData("", "제목5","이름5","13/15","2");
        homelistAdapter = new homelist_adapter(homelist);

        recyclerView = (RecyclerView) root.findViewById(R.id.listview1);
        recyclerView.setAdapter(homelistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));

        recyclerView = (RecyclerView) root.findViewById(R.id.listview2);
        recyclerView.setAdapter(homelistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));

        recyclerView = (RecyclerView) root.findViewById(R.id.listview3);
        recyclerView.setAdapter(homelistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));

        recyclerView = (RecyclerView) root.findViewById(R.id.listview4);
        recyclerView.setAdapter(homelistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));

        recyclerView = (RecyclerView) root.findViewById(R.id.listview5);
        recyclerView.setAdapter(homelistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        return root;

    }

    public void InitializeFormData(String img, String title, String name, String mans, String FID)
    {
        homelist_data tmp = new homelist_data();
        tmp.setImgName(img);
        tmp.setTitle(title);
        tmp.setName(name);
        tmp.setMans(mans);
        tmp.setFID(FID);
        homelist.add(tmp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}