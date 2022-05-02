package com.example.moamoa.ui.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moamoa.R;

public class Setting extends Fragment {

    static final String[] LIST_MENU = {"알림 설정", "앱 정보", "로그아웃", "회원 탈퇴"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setting, null);

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView) view.findViewById(R.id.set_listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "메세지", Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    Intent intent = new Intent(getActivity(), EditMyinfo.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CreatedForms fragment2 = new CreatedForms();
                    transaction.add(R.id.my_page, fragment2);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                if (position == 2) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CreatedForms fragment2 = new CreatedForms();
                    transaction.replace(R.id.my_page, fragment2);
                    transaction.addToBackStack(null).commit();
                }
                if (position == 3) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CreatedForms fragment2 = new CreatedForms();
                    transaction.replace(R.id.my_page, fragment2);
                    transaction.addToBackStack(null).commit();
                }
            }
        });

        return view;
    }
}
