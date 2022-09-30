package com.example.moamoa.ui.search;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
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
import com.example.moamoa.ui.category.CustomListView;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

/**
 * @file SearchActivity
 * @author 유진
 * @brief 게시글을 제목/제목+내용/작성자 이름 기준으로 검색
 * 카테고리/상태/정렬기준 선택 후 검색 버튼 누르면 선택한 카테고리에 해당되는 게시글 목록 출력
 */
public class SearchActivity extends AppCompatActivity {

    private ArrayList<Form> arrayList;      // 리스트 안에 담을 데이터들을 저장
    private CustomListView customListView;  // 리스트뷰 어댑터

    private EditText EditText_search;
    private static String search_std, search_input;    // std: 검색기준, input: EditText_search 텍스트화
    private Button search_btn;

    final int DIALOG_CATEGORY = 1;
    final int DIALOG_STATE = 2;
    final int DIALOG_SORT = 3;

    private Button SelectCategory, SelectState, SortBtn;

    static ArrayList<Integer> my_category = new ArrayList<>();   // 카테고리 숫자 담을 리스트
    static ArrayList<Integer> my_state = new ArrayList<>();      // 게시글 상태 숫자 담을 리스트
    static int sort_std = 0;

    ListView listView;  // 리스트뷰 연결 예정

    private final HashMap<Integer, String> sortStdHashmap = new HashMap<>();
    // 다이얼로그 번호와 정렬에 필요한 String 넣을 해시맵

    private Query sortedQuery;  // 데이터 정렬한 쿼리 넣을 예정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 검색 기준 설정 (제목/사용자닉네임)
        Spinner spinner = findViewById(R.id.searchspinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                search_std = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 카테고리 목록 불러옴, 체크박스 전부 체크, 리스트에 추가 (초기값)
        String[] category = getMy_Category();
        boolean[] cat_checkbox = allCheck_CB(category);
        my_category = returnCheckBox(cat_checkbox);

        // 상태 목록 불러옴, 체크박스 전부 체크, 리스트에 추가 (초기값)
        Resources res = getResources();
        String[] state = res.getStringArray(R.array.state);
        boolean[] state_checkbox = {true, true, false};
        my_state = returnStateCheckBox(state_checkbox);

        // 정렬 기준 설정
        sortStdHashmap.put(0, "today");
        sortStdHashmap.put(1, "parti_num");
        sortStdHashmap.put(2, "deadline");

        // 불러오기--> sortStdHashmap.get(sort_std) 하면될듯

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

        // 데이터 정렬한 쿼리
        sortedQuery = FirebaseDatabase.getInstance().getReference("form")
                .orderByChild(Objects.requireNonNull(sortStdHashmap.get(sort_std)));

        // 리스트뷰 정의
        listView = findViewById(R.id.search_listview);

        // 입력 텍스트
        EditText_search = findViewById(R.id.searchWord);

        // 리스트 모두 가져오기 (초기 상태)
        search_std = "제목";  // search_std 초기화
        search_input = "";
        SortBtn.setText("최신순"); // 정렬 기준 버튼 이름 최신순으로 변경
        search(search_std);

        // 검색 버튼을 누르면 해당되는 게시물 불러옴
        search_btn = findViewById(R.id.searchBtn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(search_std);
            }
        });
    }
    /**
     * 게시글 리스트 adapter에 부착해 출력함
     * @param sort_std 정렬 기준
     */
    private void printlistview(int sort_std){
        switch (sort_std){
            case 0: // 최신순
                Collections.reverse(arrayList);
                customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                break;
            case 1: // 인기순
                Collections.reverse(arrayList);
                customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                break;
            case 2: // 마감순

                customListView = new CustomListView(arrayList); // 어댑터 지정 (각 리스트들의 정보들 관리)
                listView.setAdapter(customListView);            // 리스트뷰의 어댑터 지정
                break;
        }
    }

    /**
     * 게시글 리스트 출력
     * search_input 비었을 시 전체 게시글 출력
     * 아닐 시 각 기준에 따라 게시글 출력
     * @param search_std 검색 기준
     */
    private void search(String search_std){
        // 검색할 텍스트 search_input에 저장
        search_input = EditText_search.getText().toString();

        // search_input 비었을 시 전체 리스트 출력
        if (search_input.equals("")){
            arrayList = new ArrayList<>();
            sortedQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else{
                        arrayList.clear();
                        DataSnapshot result = task.getResult();
                        for (DataSnapshot dataSnapshot : result.getChildren()){
                            Form form = dataSnapshot.getValue(Form.class);
                            // 카테고리 숫자 cat에 저장
                            assert form != null;
                            int cat = form.category_text;
                            int sta = form.state;
                            int act = form.active;

                            // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                            if (my_category.contains(cat) && my_state.contains(sta) && act==0){
                                arrayList.add(form);
                            }
                        }
                        printlistview(sort_std);
                    }
                }
            });
        } else{ // search_input 포함하는 게시글 찾아 목록에 추가
            switch (search_std) {
                case "제목":  // 제목 기준 게시물 검색
                    arrayList = new ArrayList<>();
                    sortedQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else{
                                arrayList.clear();
                                DataSnapshot result = task.getResult();
                                for (DataSnapshot dataSnapshot : result.getChildren()) {
                                    Form form = dataSnapshot.getValue(Form.class);

                                    // 카테고리 숫자 cat에 저장
                                    assert form != null;
                                    int cat = form.category_text;
                                    int sta = form.state;
                                    int act = form.active;

                                    // 키워드가 제목에 있으면 add
                                    // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                                    if (my_category.contains(cat) && my_state.contains(sta) && act==0
                                            && form.subject.contains(search_input)) {
                                        arrayList.add(form);
                                    }
                                }
                                printlistview(sort_std);
                            }
                        }
                    });
                    break;

                case "제목+내용":   // 제목+내용 기준 게시물 검색
                    arrayList = new ArrayList<>();
                    sortedQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else{
                                arrayList.clear();
                                DataSnapshot result = task.getResult();
                                // for문 돌려서 해당 키워드가 제목에 있는지 검색
                                for (DataSnapshot dataSnapshot : result.getChildren()) {
                                    Form form = dataSnapshot.getValue(Form.class);

                                    // 카테고리 숫자 cat에 저장
                                    assert form != null;
                                    int cat = form.category_text;
                                    int sta = form.state;
                                    int act = form.active;

                                    // 키워드가 제목에 있으면 add
                                    // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                                    if ((form.subject.contains(search_input) || form.text.contains(search_input))
                                            && my_category.contains(cat) && my_state.contains(sta) && act==0) {
                                        arrayList.add(form);
                                    }
                                }
                                printlistview(sort_std);
                            }
                        }
                    });
                    break;

                case "작성자 이름":  // 작성자 이름 기준 게시물 검색
                    // form 불러옴-> form UID로 users 불러옴-> 닉네임 불러와서 검색
                    arrayList = new ArrayList<>();
                    sortedQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else{
                                arrayList.clear();
                                DataSnapshot result1 = task.getResult();
                                for (DataSnapshot dataSnapshot : result1.getChildren()) {
                                    Form form = dataSnapshot.getValue(Form.class);

                                    // 카테고리 숫자 cat에 저장
                                    assert form != null;
                                    int cat = form.category_text;
                                    int sta = form.state;
                                    int act = form.active;

                                    // UID_dash로 닉네임 검색 위해 users 불러옴
                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(form.UID_dash)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.e("firebase", "Error getting data", task.getException());
                                                    } else{
                                                        DataSnapshot result2 = task.getResult();
                                                        User user = result2.getValue(User.class);

                                                        // 닉네임이 맞을 시 add
                                                        // 선택한 카테고리 리스트에 해당 글 카테고리 숫자가 포함될 경우 출력
                                                        if (my_category.contains(cat) && my_state.contains(sta) && act==0) {
                                                            assert user != null;
                                                            if (user.nick.equals(search_input)) {
                                                                arrayList.add(form);

                                                                // 현 문제: 추가는 됐는데 화면에 안나옴
                                                                // No setter/field found on class

                                                                // 해결(밑문장 위치 옮김)
                                                            }
                                                        }
                                                        printlistview(sort_std);
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
                    break;
            }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String FID = arrayList.get(position).FID;
                String title = arrayList.get(position).subject;

                // 액티비티 이동 + 값 전달
                Intent intent = new Intent(SearchActivity.this, FormdetailActivity.class);
                intent.putExtra("FID", FID);    //input값 intent로 전달
                intent.putExtra("UID_dash",arrayList.get(position).UID_dash);
                startActivity(intent);
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
            // 카테고리 다이얼로그
            case DIALOG_CATEGORY:
                // 카테고리 목록 불러옴, 체크박스 모두 체크
                final String[] items = getMy_Category();
                final boolean[] checkedItems = allCheck_CB(items);

                androidx.appcompat.app.AlertDialog.Builder builder1
                        = new androidx.appcompat.app.AlertDialog.Builder(SearchActivity.this);

                // 제목 설정
                builder1.setTitle("카테고리");
                //        .setCancelable(false);

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
                        search(search_std);
                    }
                });

                return builder1.create();

            case DIALOG_STATE:

                // 상태 리스트 불러옴, 체크박스 모두 체크
                final String[] states = res.getStringArray(R.array.state);
                final boolean[] checked_state = {true, true, false};

                androidx.appcompat.app.AlertDialog.Builder builder2
                        = new androidx.appcompat.app.AlertDialog.Builder(SearchActivity.this);

                // 제목 설정
                builder2.setTitle("게시글 진행 상태");
                //        .setCancelable(false);

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
                        search(search_std);
                    }
                });

                return builder2.create();

            case DIALOG_SORT:

                final String[] sortstd = res.getStringArray(R.array.sortstd);

                androidx.appcompat.app.AlertDialog.Builder builder3
                        = new androidx.appcompat.app.AlertDialog.Builder(SearchActivity.this);

                builder3.setTitle("정렬 기준");

                builder3.setSingleChoiceItems(sortstd, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SearchActivity.this, sortstd[which], Toast.LENGTH_SHORT).show();
                        sort_std = Arrays.asList(sortstd).indexOf(sortstd[which]);
                        SortBtn.setText(sortstd[which]);

                        dialog.dismiss(); // 누르면 바로 닫히는 형태
                        search(search_std);
                    }
                });

                return builder3.create();
        }
        return super.onCreateDialog(id);
    }
}