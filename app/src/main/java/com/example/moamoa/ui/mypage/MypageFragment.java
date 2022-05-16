package com.example.moamoa.ui.mypage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.example.moamoa.MainActivity;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentMyPageBinding;

public class MypageFragment extends Fragment {

    static final String[] LIST_MENU1 = {"✎  만든 폼 관리", "✔  참여한 폼 관리", "♡  관심 목록"} ;
    static final String[] LIST_MENU2 = {"\uD83D\uDC64  내 정보 수정", "⚙  환경설정"} ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_page, null) ;

        ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU1){
        //리스트뷰 글씨색 바꾸기
         @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setTextColor(Color.WHITE);
                        return view;
                    }
        };
        ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU2) ;

        ListView listview1 = (ListView) view.findViewById(R.id.listView1) ;
        listview1.setAdapter(adapter1) ;

        ListView listview2 = (ListView) view.findViewById(R.id.listView2) ;
        listview2.setAdapter(adapter2) ;

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "메세지", Toast.LENGTH_SHORT).show();
                if(position == 0){
                    Intent intent = new Intent(getActivity(), CreatedForms.class);
                    startActivity(intent);
                }
                if(position == 1){
                    Intent intent = new Intent(getActivity(), EditMyinfo.class);
                    startActivity(intent);
                }
                if(position == 2) {
                    Intent intent = new Intent(getActivity(), EditMyinfo.class);
                    startActivity(intent);
                }
            }
        });

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "메세지", Toast.LENGTH_SHORT).show();
                if(position == 0){
                    Intent intent = new Intent(getActivity(), EditMyinfo.class);
                    startActivity(intent);
                }
                if(position == 1){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Setting setting = new Setting();
//                    transaction.replace(R.id.my_page, setting);
                    transaction.add(R.id.my_page, setting);
                    transaction.addToBackStack(null).commit();
                }
            }
        });

        return view ;
    }

}


