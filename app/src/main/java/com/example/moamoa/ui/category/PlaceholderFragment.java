package com.example.moamoa.ui.category;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.CreatedFormsBinding;
import com.example.moamoa.databinding.EmptyFormsBinding;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */

/**
 * 공동구매 리스트에서 데이터 출력하기 위해 데이터 리스트에 저장 후 CustomListView로 이동
 */

public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    private EmptyFormsBinding binding;  //empty_forms를 viewpager에 binding
    int[] ca_num;
    ArrayList<Form> listViewData = new ArrayList<>();
    Form  listData;

    // 리스트 함수로 넣으려고 뺌
    ListView listView;
    int pos;

    private String[] sortstring;                // 정렬 위한 배열
    private static int sortstd_cat = 0;         // 배열 인덱스: 상태 저장 위해 여기서 static으로 값 줌
    private Query sortedQuery;                  // 데이터 정렬한 쿼리 넣을 예정

    private boolean selected = false;           // 버튼 선택된 상태

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //상세 카테고리 탭 번호
        pos = getArguments().getInt(ARG_SECTION_NUMBER);

        binding = EmptyFormsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ca_num = new int[8];
        //추가

        // 정렬 기준 설정
        sortstring = new String[]{"today", "parti_num", "deadline"};    // 최신순 - 인기순 - 마감순

        // 데이터 정렬한 쿼리
        sortedQuery = FirebaseDatabase.getInstance().getReference("form")
                .orderByChild(sortstring[sortstd_cat]);

        // 리스트뷰 연결
        listView = root.findViewById(R.id.listview);
        Button button_dead  = (Button) root.findViewById(R.id.sort_dead);   //마감순
        Button button_hot   = (Button) root.findViewById(R.id.sort_hot);    //인기순
        Button button_new   = (Button) root.findViewById(R.id.sort_new);    //최신순

        // 기본 상태 설정: 최초에는 최신순으로 정렬
        button_dead.setSelected(false);
        button_hot.setSelected(false);
        button_new.setSelected(true);
        printcatlist(sortstd_cat);

        button_dead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_dead.setSelected(true);
                button_hot.setSelected(false);
                button_new.setSelected(false);

                sortstd_cat = 2;
                printcatlist(sortstd_cat);
            }
        });
        button_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_dead.setSelected(false);
                button_hot.setSelected(true);
                button_new.setSelected(false);

                sortstd_cat = 1;
                printcatlist(sortstd_cat);
            }
        });
        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_dead.setSelected(false);
                button_hot.setSelected(false);
                button_new.setSelected(true);

                sortstd_cat = 0;
                printcatlist(sortstd_cat);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 카테고리 리스트 출력
     * @param sortstd_cat 정렬 기준 - 0일시 최신순, 1일시 인기순, 2일시 마감순
     */
    private void printcatlist(int sortstd_cat){
        sortedQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else{
                    listViewData.clear();
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        listData = snapshot.getValue(Form.class);

                        assert listData != null;
                        int act = listData.active;

                        Log.d("MainActivity", "ChildEventListener -  : " + listData);
                        switch(pos){
                            case 1:
                                if (act == 0)
                                    listViewData.add(listData);
                                break;

                            case 2:
                                if (act == 0)
                                    listViewData.add(listData);
                                break;

                            default:
                                if(listData.category_text==pos-1 && listData.getstate()==0 && act == 0){
                                    listViewData.add(listData);
                                }
                        }

                        // 기준에 따라 리스트 어댑터에 부착
                        ListAdapter oAdapter;
                        switch (sortstd_cat){
                            case 0 : // 최신순
                                Collections.reverse(listViewData);              // 내림차순
                                oAdapter = new CustomListView(listViewData);    // 어댑터 지정 (각 리스트들의 정보들 관리)
                                listView.setAdapter(oAdapter);                  // 리스트뷰의 어댑터 지정
                                break;
                            case 1: // 인기순
                                Collections.reverse(listViewData);              // 내림차순
                                oAdapter = new CustomListView(listViewData);    // 어댑터 지정 (각 리스트들의 정보들 관리)
                                listView.setAdapter(oAdapter);                  // 리스트뷰의 어댑터 지정
                                break;
                            case 2: // 마감순
                                                                                // 오름차순
                                oAdapter = new CustomListView(listViewData);    // 어댑터 지정 (각 리스트들의 정보들 관리)
                                listView.setAdapter(oAdapter);                  // 리스트뷰의 어댑터 지정
                                break;
                        }
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String FID = listViewData.get(position).FID;
                Log.e("e",listViewData.get(position).UID_dash+"");
                String title = listViewData.get(position).subject;
                //인텐트 선언 및 정의
                Intent intent = new Intent(getContext(), FormdetailActivity.class);
                //입력한 input값을 intent로 전달한다.
                intent.putExtra("FID", FID);
                intent.putExtra("UID_dash",listViewData.get(position).UID_dash);
                //액티비티 이동
                startActivity(intent);
                //Toast.makeText (getContext(), "FID : "+FID, Toast.LENGTH_SHORT).show ();
            }
        });
    }
}
