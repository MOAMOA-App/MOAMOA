package com.example.moamoa.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityChatsBinding;
import com.example.moamoa.databinding.ActivitySearchBinding;
import com.example.moamoa.databinding.EmptyFormsBinding;
import com.example.moamoa.ui.category.CustomListView;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    //private ActivitySearchBinding binding;
    SearchFragment searchFragment = new SearchFragment();
    ActivitySearchBinding binding;

    private ListView listView;  // 리스트뷰와 연결
    private ArrayList<Form> arrayList;  // 리스트 안에 담을 데이터들을 저장
    private CustomListView customListView;  // 리스트뷰 어댑터

    private EditText EditText_search;
    private String search_std, search_input;
    private Button search_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        // 프래그먼트 매니저로 searchcontainer에 searchFragment 연결해줌
        //getSupportFragmentManager().beginTransaction().replace(R.id.chatscontainer, searchFragment).commit();

        // 검색 기준 설정 (제목/사용자닉네임)
        Spinner spinner = findViewById(R.id.searchspinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                search_std = adapterView.getItemAtPosition(position).toString();
                Log.e("TEST", "search_std: "+search_std);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 리스트뷰 정의
        listView = root.findViewById(R.id.search_listview);

        // 검색 버튼을 누르면 해당되는 게시물 불러옴
        EditText_search = findViewById(R.id.searchWord);
        search_btn = findViewById(R.id.searchBtn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색활 텍스트 search_input에 저장
                search_input = EditText_search.getText().toString();
                Log.e("TEST", "search_input: "+search_input);

                // 검색 전에 일단 모든 게시물 불러오기
                arrayList = new ArrayList<>();
                FirebaseDatabase.getInstance().getReference("form")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                arrayList.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Toast.makeText(getApplicationContext(), "버튼 누름", Toast.LENGTH_SHORT).show();
                                    Form form = snapshot.getValue(Form.class);

                                    arrayList.add(form);
                                    // 오류(추가안됨). Log:
                                    // No setter/field for FormID found on class com.example.moamoa.Form

                                    /* 추가하고 싶은 코드
                                    if (form.subject.contains(search_input))
                                        arrayList.add(form);

                                     */
                                    customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                                    listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                /*
                // 검색창에 넣은 텍스트 사용
                search_input = EditText_search.getText().toString();

                // 각각 제목/작성자이름 기준으로 검색 예정
                // search_input 포함하는 게시글 찾아 목록에 추가
                if (search_std.equals("제목")){
                    Toast.makeText(getApplicationContext(), "제목 기준", Toast.LENGTH_SHORT).show();
                    Log.e("TEST", "search_input: "+search_input);

                    FirebaseDatabase.getInstance().getReference("form")
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayList.clear();
                            Form form = snapshot.getValue(Form.class);
                            assert form != null;

                            if (form.subject.contains(search_input))
                                arrayList.add(form);

                            ListAdapter oAdapter = new CustomListView(arrayList);
                            listView.setAdapter(oAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String FID = arrayList.get(position).FID;
                            String title = arrayList.get(position).subject;

                            //인텐트 선언 및 정의


                            Intent intent = new Intent(getApplicationContext(), FormdetailActivity.class);
                            //입력한 input값을 intent로 전달한다.
                            intent.putExtra("FID", FID);
                            //액티비티 이동
                            startActivity(intent);
                            //Toast.makeText (getContext(), "FID : "+FID, Toast.LENGTH_SHORT).show ();
                        }
                    });
                } else if (search_std.equals("이름")){
                    Toast.makeText(getApplicationContext(), "작성자 이름 기준", Toast.LENGTH_SHORT).show();
                    Log.e("TEST", "search_input: "+search_input);

                    FirebaseDatabase.getInstance().getReference("form")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    arrayList.clear();
                                    Form form = snapshot.getValue(Form.class);
                                    assert form != null;
                                    if (form.subject.contains(search_input))
                                        arrayList.add(form);

                                    ListAdapter oAdapter = new CustomListView(arrayList);
                                    listView.setAdapter(oAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String FID = arrayList.get(position).FID;
                            String title = arrayList.get(position).subject;

                            //인텐트 선언 및 정의


                            Intent intent = new Intent(getApplicationContext(), FormdetailActivity.class);
                            //입력한 input값을 intent로 전달한다.
                            intent.putExtra("FID", FID);
                            //액티비티 이동
                            startActivity(intent);
                            //Toast.makeText (getContext(), "FID : "+FID, Toast.LENGTH_SHORT).show ();
                        }
                    });
                }

                 */
            }
        });


    }
}