package com.example.moamoa.ui.mypage;

import android.app.Activity;
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

public class Setting extends Activity {

    static final String[] LIST_MENU = {"알림 설정", "앱 정보", "로그아웃", "회원 탈퇴"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView) findViewById(R.id.set_listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "메세지", Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    //string 부분 틀림
                    Intent intent = new Intent(String.valueOf(EditMyinfo.class));
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(String.valueOf(EditMyinfo.class));
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(String.valueOf(EditMyinfo.class));
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(String.valueOf(EditMyinfo.class));
                    startActivity(intent);
                }
            }
        });
    }
}
