package com.example.moamoa.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.category.CategoryData;
import com.example.moamoa.ui.category.CustomListView;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<Form> arrayList;  // 리스트 안에 담을 데이터들을 저장
    private CustomListView customListView;  // 리스트뷰 어댑터

    private EditText EditText_search;
    private String search_std, search_input;
    private Button search_btn;

    private Button SelectCategory, SelectState;
    private DatabaseReference mDatabase;
    private List<String> name_list = new ArrayList<>();
    private List<Integer> numb_list = new ArrayList<>();

    boolean[] my_list = new boolean[15];
    List<Integer> my_category = new ArrayList<>();
    List<Integer> my_state = new ArrayList<>();

    static String[] category = new String[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 카테고리 목록 불러옴
        category = getMy_Category();
        Log.e("TEST_4", "category: "+ Arrays.toString(category));


        // 카테고리 선택
        /*
        일단 뭘어케하면되냐면... 일단 여기서 선택을 해놔 체크박스에 다 체크된상태로
        저거를 리스트에 담아놓고 저 밑 코드에서 이제 리스트에 담긴 카테고리만 출력하도록 하는거지
        그럼 생각할게
         - 리스트에 담을 방법
         - 리스트에서 뺄 방법
         - 리스트에 담긴 카테고리만 출력할 방법
        앞을 생각하면 뒤는 따라올듯?
        담을 방법은 일단... 파이어베이스에서 카테고리 목록 불러와서 리스트에 넣어야됨
        리스트에 담긴 카테고리만 출력하는거는 for문 돌려서 글 카테고리가 리스트에 담긴 카테고리에 있는지 보면 될거고

        담고빼기는 했는데 이제 이걸 어케... 출력하냐 지금 리스트가 다 string으로 되어있는데
        그럼 category 연결해서 이름을 찾아와서 검색을 해야하나 스으읍
         */
        mDatabase = FirebaseDatabase.getInstance().getReference();

        SelectCategory = findViewById(R.id.select_category);
        SelectState = findViewById(R.id.select_state);

        // 버튼 누르면 카테고리 목록 출력
        SelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0); // 다이얼로그 호출
            }
        });

        // 리스트뷰 정의
        ListView listView = findViewById(R.id.search_listview);

        // 리스트 모두 가져오기
        arrayList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Form form = dataSnapshot.getValue(Form.class);
                    arrayList.add(form);
                    Log.e("TEST", "form: "+form);
                }
                customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String FID = arrayList.get(position).FID;
                String title = arrayList.get(position).subject;

                // 액티비티 이동 + 값 전달
                Intent intent = new Intent(SearchActivity.this, FormdetailActivity.class);
                intent.putExtra("FID", FID);    //input값 intent로 전달
                startActivity(intent);
            }
        });

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

        // 검색 버튼을 누르면 해당되는 게시물 불러옴
        EditText_search = findViewById(R.id.searchWord);
        search_btn = findViewById(R.id.searchBtn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색할 텍스트 search_input에 저장
                search_input = EditText_search.getText().toString();
                Log.e("TEST", "search_input: "+search_input);   // 삭제예정코드(확인용)

                // 각각 제목/작성자이름 기준으로 검색
                // search_input 포함하는 게시글 찾아 목록에 추가

                // 제목 기준 게시물 검색
                if (search_std.equals("제목")){
                    Toast.makeText(SearchActivity.this, "제목 기준", Toast.LENGTH_SHORT).show();
                    Log.e("TEST", "search_input: "+search_input);

                    arrayList = new ArrayList<>();
                    FirebaseDatabase.getInstance().getReference().child("form").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayList.clear();
                            // for문 돌려서 해당 키워드가 제목에 있는지 검색
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Form form = dataSnapshot.getValue(Form.class);
                                // 키워드가 제목에 있으면 add
                                if (form.subject.contains(search_input)){
                                    arrayList.add(form);
                                    Log.e("TEST", "form: "+form);
                                }
                            }
                            customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                            listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SearchActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    /*
                    FirebaseDatabase.getInstance().getReference().child("form").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayList.clear();
                            // for문 돌려서 해당 키워드가 제목에 있는지 검색
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Form form = dataSnapshot.getValue(Form.class);

                                String cat1 = Integer.toString(form.category_text);
                                Log.e("TEST_3", "cat1: "+cat1);

                                FirebaseDatabase.getInstance().getReference("category")
                                        .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        CategoryData categoryData
                                                = snapshot.getValue(CategoryData.class);
                                        String cat2 = categoryData.name;
                                        Log.e("TEST_3", "cat2: "+cat2);

                                        if (form.subject.contains(search_input)){
                                            for (int i=0;i<category.length;i++){
                                                if (cat2.equals(category[i])){
                                                    arrayList.add(form);
                                                    Log.e("TEST", "form: "+form);
                                                }
                                            }

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                                // 키워드가 제목에 있으면 add
                                if (form.subject.contains(search_input)){
                                    arrayList.add(form);
                                    Log.e("TEST", "form: "+form);
                                }
                            }
                            customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                            listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SearchActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                     */
                    /*
                    FirebaseDatabase.getInstance().getReference().child("form").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayList.clear();
                            Form form = snapshot.getValue(Form.class);
                            String cat1 = Integer.toString(form.category_text);
                            Log.e("TEST_3", "cat1: "+cat1);

                            // for문 돌려서 해당 키워드가 제목에 있는지 검색
                            FirebaseDatabase.getInstance().getReference("category").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        CategoryData categoryData = snapshot.getValue(CategoryData.class);

                                        String cat2 = categoryData.getName();
                                        Log.e("TEST_3", "cat2: "+cat2);

                                        // 키워드가 제목에 있으면 add
                                        if (form.subject.contains(search_input)){
                                            arrayList.add(form);
                                            Log.e("TEST", "form: "+form);
                                        }

                                        // 키워드가 제목에 있으면 add
                                        /*
                                        if (form.subject.contains(search_input)){
                                            for (int i=0;i<category.length;i++){
                                                if (cat2.equals(category[i])){
                                                    arrayList.add(form);
                                                    Log.e("TEST", "form: "+form);
                                                }
                                            }

                                        }

                                         */
                    /*
                                    }
                                    customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                                    listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SearchActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                     */
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String FID = arrayList.get(position).FID;
                            String title = arrayList.get(position).subject;

                            // 액티비티 이동 + 값 전달
                            Intent intent = new Intent(SearchActivity.this, FormdetailActivity.class);
                            intent.putExtra("FID", FID);    //input값 intent로 전달
                            startActivity(intent);
                        }
                    });
                }

                // 제목+내용 기준 게시물 검색
                else if (search_std.equals("제목+내용")){
                    Toast.makeText(SearchActivity.this, "제목+내용 기준", Toast.LENGTH_SHORT).show();
                    Log.e("TEST", "search_input: "+search_input);

                    arrayList = new ArrayList<>();
                    FirebaseDatabase.getInstance().getReference().child("form").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayList.clear();
                            // for문 돌려서 해당 키워드가 제목에 있는지 검색
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Form form = dataSnapshot.getValue(Form.class);
                                // 키워드가 제목에 있으면 add
                                if (form.subject.contains(search_input) || form.text.contains(search_input)){
                                    arrayList.add(form);
                                    Log.e("TEST", "form: "+form);
                                }
                            }
                            customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                            listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SearchActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String FID = arrayList.get(position).FID;
                            String title = arrayList.get(position).subject;

                            // 액티비티 이동 + 값 전달
                            Intent intent = new Intent(SearchActivity.this, FormdetailActivity.class);
                            intent.putExtra("FID", FID);    //input값 intent로 전달
                            startActivity(intent);
                        }
                    });
                }

                // 작성자 이름 기준 게시물 검색
                else if (search_std.equals("이름")){
                    Toast.makeText(SearchActivity.this, "작성자 이름 기준", Toast.LENGTH_SHORT).show();
                    Log.e("TEST", "search_input: "+search_input);

                    // 이거어떡해야되냐면... 일단 폼을 불러와 불러오는데 이제 거기 UID를 가지고 유저 검색을 해서 이게 이 닉네임이
                    // 엥 아니지? 그냥 UID가 맞는지 찾아보면 되는거잖아
                    // 그니까 작성자를 찾아봐 아 습 아닌가... 일단 닉네임으로 검색을 하는거잖아 그럼
                    // 어니다 그냥 아이디랑 닉네임 일일이 대조하는게 나을듯...
                    // 일단 for문을 돌린 뒤에 그 안에서 검색하면 되나

                    FirebaseDatabase.getInstance().getReference().child("form").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayList.clear();
                            // for문 돌려서 해당 키워드와 작성자 이름이 맞는 게시물이 있는지 검색
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Form form = dataSnapshot.getValue(Form.class);

                                // UID_dash로 닉네임 검색 위해 users 불러옴
                                FirebaseDatabase.getInstance().getReference().child("users").child(form.UID_dash)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                User user = snapshot.getValue(User.class);
                                                Log.e("TEST1", "userUID: "+user);

                                                // 닉네임이 맞을 시 add
                                                if (user.nick.equals(search_input)){
                                                    Log.e("TEST2", "usernick: "+user.nick);
                                                    arrayList.add(form);
                                                    Log.e("TEST3", "form: "+form);

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
                            Toast.makeText(SearchActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String FID = arrayList.get(position).FID;
                            String title = arrayList.get(position).subject;

                            // 액티비티 이동 + 값 전달
                            Intent intent = new Intent(SearchActivity.this, FormdetailActivity.class);
                            intent.putExtra("FID", FID);    //input값 intent로 전달
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private String[] getMy_Category(){  // 카테고리 목록 불러오기
        Resources res = getResources();
        final String[] item = res.getStringArray(R.array.category);
        // 카테고리 목록에서 전체/관심 뺌
        final String[] items = new String[10];
        for (int i = 0; i<item.length-2; i++){
            items[i] = item[i+2];
            Log.e("TEST_4", "items["+i+"]: "+items[i]);
        }
        Log.e("TEST_4", "items: "+ Arrays.toString(items));
        return items;
    }

    protected Dialog onCreateDialog(int id) {

        String[] items = getMy_Category();

        // 체크박스 모두 체크
        final boolean[] checkedItems = new boolean[10];
        for (int i = 0; i<items.length; i++){
            checkedItems[i] = true;
        }
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SearchActivity.this);

        // 제목 설정
        builder.setTitle("카테고리 선택");

        // 바뀐 것 적용
        builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedItems[which] = isChecked;
            }
        });

        // 같은 onclick을 사용하기 때문에 DialogInterface 적어주어야 에러가 발생하지 않음
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                category = items;

                // 현제 체크된 항목 확인
                String str = "";
                for (int i = 0; i < items.length; i++) {
                    if (checkedItems[i]) {
                        str += items[i];
                        if (i != items.length - 1) {
                            str += ", ";
                        }
                    }
                }
                Toast.makeText(SearchActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}