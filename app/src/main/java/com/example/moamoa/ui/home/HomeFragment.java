package com.example.moamoa.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.databinding.FragmentHomeBinding;
import com.example.moamoa.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView listView;
    private formlist_adapter adapter;
    private ArrayList<formlist_data> formlist;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.InitializeFormData();

        ListView listView = (ListView) root.findViewById(R.id.listview);
        final formlist_adapter formlistAdapter = new formlist_adapter(getActivity(),formlist);
        listView.setAdapter(formlistAdapter);
        return root;
    }
    public void InitializeFormData()
    {
        formlist = new ArrayList<formlist_data>();

        formlist.add(new formlist_data(1, "30,000","제목1","이름1","22-03-12"));
        formlist.add(new formlist_data(1, "30,000","제목2","이름1","22-03-12"));
        formlist.add(new formlist_data(1, "30,000","제목3","이름1","22-03-12"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}