package com.example.moamoa.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityChatsBinding;
import com.example.moamoa.databinding.ActivitySearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    SearchFragment searchFragment = new SearchFragment();

    RecyclerView recyclerView;

    private EditText EditText_search;
    private String search_std, search_input;
    private Button search_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance())
                    .commitNow();
        }

         */

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        // 프래그먼트 매니저로 searchcontainer에 searchFragment 연결해줌
        //getSupportFragmentManager().beginTransaction().replace(R.id.chatscontainer, searchFragment).commit();

        // 검색 기준 설정 (제목 / 사용자닉네임)
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

        // 리사이클러뷰 정의
        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        recyclerView.setHasFixedSize(true);

        // 검색 버튼을 누르면 해당되는 게시물 불러옴
        ArrayList<Form> arrayList = new ArrayList<>();
        EditText_search = findViewById(R.id.searchWord);
        search_btn = findViewById(R.id.searchBtn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색창에 넣은 텍스트 사용
                search_input = EditText_search.getText().toString();

                // 각각 제목/작성자이름 기준으로 검색 예정
                if (search_std.equals("제목")){
                    Toast.makeText(getApplicationContext(), "제목 기준", Toast.LENGTH_SHORT).show();
                    Log.e("TEST", "search_input: "+search_input);

                    FirebaseDatabase.getInstance().getReference("form")
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else{
                    Toast.makeText(getApplicationContext(), "작성자 이름 기준", Toast.LENGTH_SHORT).show();
                    Log.e("TEST", "search_input: "+search_input);

                    FirebaseDatabase.getInstance().getReference("form")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        });


    }
    class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}