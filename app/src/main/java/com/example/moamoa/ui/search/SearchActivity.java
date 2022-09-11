package com.example.moamoa.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.moamoa.databinding.FragmentSearchBinding;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.category.CustomListView;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @file SearchActivity
 * @author 유진
 * @brief 게시글을 제목/제목+내용/작성자 이름 기준으로 검색
 * 카테고리/상태/정렬기준 선택 후 검색 버튼 누르면 선택한 카테고리에 해당되는 게시글 목록 출력
 */
public class SearchActivity extends AppCompatActivity {

    FragmentSearchBinding binding;
    SearchFragment searchFragment = new SearchFragment();

    private ArrayList<Form> arrayList;      // 리스트 안에 담을 데이터들을 저장
    private CustomListView customListView;  // 리스트뷰 어댑터

    private EditText EditText_search;
    private static String search_std, search_input;    // std: 검색기준, input: EditText_search 텍스트화
    private Button search_btn;

    final int DIALOG_CATEGORY = 1;
    final int DIALOG_STATE = 2;
    final int DIALOG_SORT = 3;

    private Button SelectCategory, SelectState, SortBtn;

    Form listData;

    static ArrayList<Integer> my_category = new ArrayList<>();   // 카테고리 숫자 담을 리스트
    static ArrayList<Integer> my_state = new ArrayList<>();      // 게시글 상태 숫자 담을 리스트
    static int sort_std;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*
        * 오류남!! 코드 오류나길래 일부러 옛날걸로해서 복붙했는데 왜오류나는지 모르겠음
        * 로그
        * java.lang.ClassCastException: com.example.moamoa.ui.search.SearchActivity
        * cannot be cast to com.example.moamoa.ui.category.CategoryActivity
        at com.example.moamoa.ui.category.CustomListView$1.onSuccess(CustomListView.java:80)
        at com.example.moamoa.ui.category.CustomListView$1.onSuccess(CustomListView.java:77)
        * */

        // 검색 기준 설정 (제목/사용자닉네임)
        Spinner spinner = findViewById(R.id.searchspinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                search_std = adapterView.getItemAtPosition(position).toString();
                Log.e("TEST5", "search_std1: "+search_std);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.e("TEST5", "search_std2: "+search_std);



        // 카테고리 목록 불러옴, 체크박스 전부 체크, 리스트에 추가 (초기값)
        String[] category = getMy_Category();
        boolean[] cat_checkbox = allCheck_CB(category);
        my_category = returnCheckBox(cat_checkbox);

        // 상태 목록 불러옴, 체크박스 전부 체크, 리스트에 추가 (초기값)
        Resources res = getResources();
        String[] state = res.getStringArray(R.array.state);
        boolean[] state_checkbox = allCheck_CB(state);
        my_state = returnStateCheckBox(state_checkbox);

        // 게시글 정렬 기준 초기값: 최신순
        sort_std = 0;

        // 만약 모두 체크되어있지 않으면 getExtra를 불러오기
        // 그거를 어케아느냐... 1. 체크박스가 모두 true인지 보기 2. my_category 길이가 category 길이인지 보기

        // 아니지 일단 받아와서 만약 null이면 모두 채우기 해야지
        /*
        Intent getIntent = getIntent();
        my_category = getIntent.getIntegerArrayListExtra("category");
        Log.e("TEST_3", "my_category: "+my_category);
        if (my_category == null){
            cat_checkbox = allCheck_CB(category);
            my_category = returnCheckBox(cat_checkbox);
            Log.e("TEST_2", "my_category_2: "+my_category);
        }

         */
        /*
        Log.e("TEST_3", "my_category_1: "+my_category);
        Log.e("TEST_3", "my_category_1: "+my_category);
        if (my_category.size() != category.length){
            my_category = getIntent.getIntegerArrayListExtra("category");
            Log.e("TEST_2", "my_category_1: "+my_category);
        } else {

        }

         */

        // 버튼 누르면 카테고리 목록 출력
        SelectCategory = findViewById(R.id.select_category);
        SelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_CATEGORY); // 다이얼로그 호출
            }
        });

        // 버튼 누르면 상태 목록 출력
        SelectState = findViewById(R.id.select_state);
        SelectState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_STATE); // 다이얼로그 호출
            }
        });

        // 버튼 누르면 정렬 기준 목록 출력
        SortBtn = findViewById(R.id.search_sortbtn);
        SortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_SORT); // 다이얼로그 호출
            }
        });

        /*
        // 검색할 텍스트 search_input에 저장 (초기값)
        search_input = null;

        // 프래그먼트 매니저로 chatscontainer에 searchFragment 연결해줌
        Bundle bundle = new Bundle();
        bundle.putString("searchStd", search_std);
        bundle.putString("searchInput", search_input);
        bundle.putIntegerArrayList("myCategory", my_category);
        searchFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.searchcontainer, searchFragment).commit();


         */
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
                    // 카테고리 숫자 cat에 저장
                    int cat = form.category_text;
                    int sta = form.state;

                    // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                    if (my_category.contains(cat) && my_state.contains(sta)){
                        arrayList.add(form);
                    }
                }
                /*
                listData.s_case = sort_std;
                arrayList.add(listData);
                Collections.sort(arrayList);

                 */
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



        // 검색 버튼을 누르면 해당되는 게시물 불러옴
        EditText_search = findViewById(R.id.searchWord);
        search_btn = findViewById(R.id.searchBtn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색할 텍스트 search_input에 저장
                search_input = EditText_search.getText().toString();
                /*
                Log.e("TEST5", "search_std3: "+search_std);
                Log.e("TEST5", "search_input1: "+search_input);

                // 프래그먼트 매니저로 searchcontainer에 searchFragment 연결 + 값 넘김
                Bundle bundle = new Bundle();
                bundle.putString("searchStd", search_std);
                bundle.putString("searchInput", search_input);
                bundle.putIntegerArrayList("myCategory", my_category);
                searchFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.searchcontainer, searchFragment).commit();



                 */
                // 검색할 텍스트 search_input에 저장
                search_input = EditText_search.getText().toString();

                // search_input 포함하는 게시글 찾아 목록에 추가
                switch (search_std) {
                    case "제목":  // 제목 기준 게시물 검색
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
                                    int sta = form.state;

                                    // 키워드가 제목에 있으면 add
                                    // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                                    if (my_category.contains(cat) && my_state.contains(sta)
                                            && form.subject.contains(search_input)) {
                                        arrayList.add(form);
                                        Log.e("TEST1", "my_state: "+my_state);
                                        Log.e("TEST1", "sta: "+sta);

                                    }
                                }
                                customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                                listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(SearchActivity.this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    int sta = form.state;

                                    // 키워드가 제목에 있으면 add
                                    // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                                    if ((form.subject.contains(search_input) || form.text.contains(search_input))
                                            && my_category.contains(cat) && my_state.contains(sta)) {
                                        arrayList.add(form);
                                    }
                                }
                                customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                                listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(SearchActivity.this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    int sta = form.state;

                                    // UID_dash로 닉네임 검색 위해 users 불러옴
                                    FirebaseDatabase.getInstance().getReference().child("users").child(form.UID_dash)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    User user = snapshot.getValue(User.class);

                                                    // 닉네임이 맞을 시 add
                                                    // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                                                    if (my_category.contains(cat) && my_state.contains(sta)
                                                            && user.nick.equals(search_input)) {
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
                                Toast.makeText(SearchActivity.this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(SearchActivity.this, FormdetailActivity.class);
                        intent.putExtra("FID", FID);    //input값 intent로 전달
                        startActivity(intent);
                    }
                });
            }
        });
    }

    /**
     * 카테고리 전체 목록 불러오기
     * @return items 불러온 카테고리 목록
     */
    private String[] getMy_Category(){
        Resources res = getResources();
        final String[] item = res.getStringArray(R.array.category);

        // 카테고리 목록에서 전체/관심 뺌
        String[] items = new String[10];
        for (int i = 0; i<item.length-2; i++){
            items[i] = item[i+2];
        }
        return items;
    }

    /**
     * 체크박스 모두 체크
     * @return checkedItems 카테고리 체크박스 전부 체크
     */
    private boolean[] allCheck_CB(String[] items){
        boolean[] checkedItems = new boolean[items.length];
        Arrays.fill(checkedItems, true);
        return checkedItems;
    }

    /**
     * 체크박스에서 true인 것만 골라서 리스트에 숫자로 넣음 (파이어베이스 연결을 위해 - 전체, 관심 제외)
     * @param booleans 체크박스 체크 여부 배열
     * @return my_category 값이 true면 인덱스+2를 my_category에 넣음
     */
    private ArrayList<Integer> returnCheckBox(boolean[] booleans){
        ArrayList<Integer> my_category = new ArrayList<>();
        //my_category.clear();
        for (int i = 0; i<booleans.length; i++ ){
            if (booleans[i]){
                my_category.add(i+2);
            }
        }
        return my_category;
    }

    /**
     * 체크박스에서 true인 것만 골라서 리스트에 숫자로 넣음
     * @param booleans 체크박스 체크 여부 배열
     * @return my_category 값이 true면 인덱스를 my_category에 넣음
     */
    private ArrayList<Integer> returnStateCheckBox(boolean[] booleans){
        ArrayList<Integer> list = new ArrayList<>();
        //my_category.clear();
        for (int i = 0; i<booleans.length; i++ ){
            if (booleans[i]){
                list.add(i);
            }
        }
        return list;
    }

    /**
     * 카테고리 선택 / 상태 선택 / 정렬 다이얼로그
     */
    protected Dialog onCreateDialog(int id) {
        Resources res = getResources();

        switch (id){
            case DIALOG_CATEGORY:   // 카테고리 선택
                // 카테고리 목록 불러옴, 체크박스 모두 체크
                final String[] items = getMy_Category();
                final boolean[] checkedItems = allCheck_CB(items);

                androidx.appcompat.app.AlertDialog.Builder builder1
                        = new androidx.appcompat.app.AlertDialog.Builder(SearchActivity.this);

                // 제목 설정
                builder1.setTitle("카테고리");

                // 바뀐 것 적용
                builder1.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                });

                // 같은 onclick을 사용하기 때문에 DialogInterface 적어주어야 에러가 발생하지 않음
                builder1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // true인 것만 골라서 my_category에 넣음
                        my_category = returnCheckBox(checkedItems);
                    }
                });
                return builder1.create();

            case DIALOG_STATE:  // 상태 선택
                // 상태 리스트 불러옴, 체크박스 모두 체크
                final String[] states = res.getStringArray(R.array.state);
                final boolean[] checked_state = allCheck_CB(states);

                androidx.appcompat.app.AlertDialog.Builder builder2
                        = new androidx.appcompat.app.AlertDialog.Builder(SearchActivity.this);

                // 제목 설정
                builder2.setTitle("게시글 진행 상태");

                // 바뀐 것 적용
                builder2.setMultiChoiceItems(states, checked_state, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checked_state[which] = isChecked;
                    }
                });

                builder2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        my_state = returnStateCheckBox(checked_state);
                    }
                });

                return builder2.create();

            case DIALOG_SORT:   // 게시글 정렬
                final String[] sortstd = res.getStringArray(R.array.sortstd);

                androidx.appcompat.app.AlertDialog.Builder builder3
                        = new androidx.appcompat.app.AlertDialog.Builder(SearchActivity.this);

                builder3.setTitle("정렬 기준");

                builder3.setSingleChoiceItems(sortstd, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SearchActivity.this, sortstd[which], Toast.LENGTH_SHORT).show();
                        sort_std = Arrays.asList(sortstd).indexOf(sortstd[which]);
                        Log.e("TEST2", "sort_std: "+sort_std);

                        dialog.dismiss(); // 누르면 바로 닫히는 형태
                    }
                });

                return builder3.create();
        }
        return super.onCreateDialog(id);
    }
}