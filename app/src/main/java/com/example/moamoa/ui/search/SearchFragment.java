package com.example.moamoa.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentSearchBinding;
import com.example.moamoa.ui.category.CustomListView;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    //SearchFragment searchFragment = new SearchFragment();

    private ArrayList<Form> arrayList;      // 리스트 안에 담을 데이터들을 저장
    private CustomListView customListView;  // 리스트뷰 어댑터

    private String search_std, search_input;    // std: 검색기준, input: EditText_search 텍스트화

    static ArrayList<Integer> my_category = new ArrayList<>();   // 카테고리 숫자 담을 리스트
    static List<Integer> my_state = new ArrayList<>();      // 게시글 상태 숫자 담을 리스트

    static String[] category = new String[15];          // 카테고리 목록
    static boolean[] cat_checkbox = new boolean[15];    // 카테고리 체크박스

    /*
    public SearchFragment() {
        // Required empty public constructor
    }

     */

    /*
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // SearchActivity에서 값 받아오기
        Bundle bundle = getArguments();
        search_std = bundle.getString("searchStd");
        search_input = bundle.getString("searchInput");
        my_category = bundle.getIntegerArrayList("myCategory");
        Log.e("TEST5", "search_std4: "+search_std);
        Log.e("TEST5", "search_input2: "+search_input);
        Log.e("TEST5", "my_category: "+my_category);


        // 리스트뷰 정의
        ListView listView = root.findViewById(R.id.search_listview);

        // 리스트 모두 가져오기
        arrayList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Form form = dataSnapshot.getValue(Form.class);
                    // 카테고리 숫자 cat에 저장
                    int cat = form.category_text;

                    // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                    if (my_category.contains(cat)){
                        arrayList.add(form);
                    }
                }
                customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String FID = arrayList.get(position).FID;
                String title = arrayList.get(position).subject;

                // 액티비티 이동 + 값 전달
                Intent intent = new Intent(getContext(), FormdetailActivity.class);
                intent.putExtra("FID", FID);    //input값 intent로 전달
                startActivity(intent);
            }
        });

        // search_input 포함하는 게시글 찾아 목록에 추가
        /*
        switch (search_std) {
            //case "제목":  // 제목 기준 게시물 검색
            default:
                arrayList = new ArrayList<>();
                FirebaseDatabase.getInstance().getReference().child("form").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        // for문 돌려서 해당 키워드가 제목에 있는지 검색
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Form form = dataSnapshot.getValue(Form.class);

                            // 카테고리 숫자 cat에 저장
                            int cat = form.category_text;

                            // 키워드가 제목에 있으면 add
                            // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                            if (my_category.contains(cat) && form.subject.contains(search_input)) {
                                arrayList.add(form);

                            }
                        }
                        customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                        listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case "제목+내용":   // 제목+내용 기준 게시물 검색
                arrayList = new ArrayList<>();
                FirebaseDatabase.getInstance().getReference().child("form").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        // for문 돌려서 해당 키워드가 제목에 있는지 검색
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Form form = dataSnapshot.getValue(Form.class);

                            // 카테고리 숫자 cat에 저장
                            int cat = form.category_text;

                            // 키워드가 제목에 있으면 add
                            // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                            if ((form.subject.contains(search_input) || form.text.contains(search_input))
                                    && my_category.contains(cat)) {
                                arrayList.add(form);
                            }
                        }
                        customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                        listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case "이름":  // 작성자 이름 기준 게시물 검색
                // form 불러옴-> form UID로 users 불러옴-> 닉네임 불러와서 검색
                FirebaseDatabase.getInstance().getReference().child("form").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        // for문 돌려서 해당 키워드와 작성자 이름이 맞는 게시물이 있는지 검색
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Form form = dataSnapshot.getValue(Form.class);

                            // 카테고리 숫자 cat에 저장
                            int cat = form.category_text;

                            // UID_dash로 닉네임 검색 위해 users 불러옴
                            FirebaseDatabase.getInstance().getReference().child("users").child(form.UID_dash)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = snapshot.getValue(User.class);

                                            // 닉네임이 맞을 시 add
                                            // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                                            if (my_category.contains(cat) && user.nick.equals(search_input)) {
                                                arrayList.add(form);

                                                // 현 문제: 추가는 됐는데 화면에 안나옴
                                                // No setter/field found on class

                                                // 해결(밑문장 위치 옮김)
                                            }
                                            customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                                            listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String FID = arrayList.get(position).FID;
                String title = arrayList.get(position).subject;

                // 액티비티 이동 + 값 전달
                Intent intent = new Intent(getContext(), FormdetailActivity.class);
                intent.putExtra("FID", FID);    //input값 intent로 전달
                startActivity(intent);
            }
        });


         */
        return root;
    }
}