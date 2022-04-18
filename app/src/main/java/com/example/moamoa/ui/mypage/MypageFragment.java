package com.example.moamoa.ui.mypage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.example.moamoa.MainActivity;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentMyPageBinding;

public class MypageFragment extends Fragment {

    private FragmentMyPageBinding binding;
    static final String[] LIST_MENU = {"내 정보 수정", "만든 폼 관리", "참여한 폼 관리", "찜 목록", "앱 정보", "환경설정"} ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_page, null) ;

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) view.findViewById(R.id.listView) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "메세지", Toast.LENGTH_SHORT).show();
                if(position == 0){
                    Intent intent = new Intent(getActivity(), EditMyinfo.class);
                    startActivity(intent);
                }
                if(position == 1){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CreatedForms fragment2 = new CreatedForms();
                    transaction.add(R.id.my_page, fragment2);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                if(position == 2){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CreatedForms fragment2 = new CreatedForms();
                    transaction.replace(R.id.my_page, fragment2);
                    transaction.addToBackStack(null).commit();
                }
                if(position == 3){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CreatedForms fragment2 = new CreatedForms();
                    transaction.replace(R.id.my_page, fragment2);
                    transaction.addToBackStack(null).commit();
                }
                if(position == 4){
                    Intent intent = new Intent(getActivity(), EditMyinfo.class);
                    startActivity(intent);
                }
                if(position == 5){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Setting fragment3 = new Setting();
                    transaction.replace(R.id.my_page, fragment3);
                    transaction.addToBackStack(null).commit();
                }
            }
        });

        return view ;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

