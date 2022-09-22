package com.example.moamoa.ui.home;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.Retrofit_Function;
import com.example.moamoa.databinding.FragmentHomeBinding;
import com.example.moamoa.ui.category.CategoryActivity;
import com.example.moamoa.ui.category.CategoryAdapter_my;
import com.example.moamoa.ui.category.CategoryData;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.example.moamoa.ui.notifications.NotificationsActivity;
import com.example.moamoa.ui.search.SearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    private @NonNull FragmentHomeBinding binding;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<homelist_data> homelist[]=new ArrayList[4];
    private homelist_adapter homelistAdapter[]=new homelist_adapter[4];
    private RecyclerView[] recyclerView = new RecyclerView[5];
    private FirebaseDatabase mDatabase;
    private TextView[] btn_c = new TextView[5];
    private CategoryAdapter_my categoryAdapter_my;
    private GridView gridViews;
    boolean[] my_list = new boolean[15];

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mDatabase = FirebaseDatabase.getInstance();
        Query reference[] = new Query[5];
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd");
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        categoryAdapter_my = new CategoryAdapter_my();
        gridViews = (GridView) root.findViewById(R.id.home_my_category);
        initmylist();
        /* 카테고리 grid 출력 */
        gridViews.setAdapter(categoryAdapter_my);

        /* 카테고리 Recyclerview 설정 */
        recyclerView[0] = (RecyclerView) root.findViewById(R.id.listview0); //마감순
        recyclerView[1] = (RecyclerView) root.findViewById(R.id.listview1); //인기순
        recyclerView[2] = (RecyclerView) root.findViewById(R.id.listview2); //신규순
        recyclerView[3] = (RecyclerView) root.findViewById(R.id.listview3); //관심
        //recyclerView[4] = (RecyclerView) root.findViewById(R.id.listview4);


        reference[0] = mDatabase.getReference().child("form").orderByChild("deadline").startAt(Integer.parseInt(mFormat.format(mDate)));
        reference[1] = mDatabase.getReference().child("form").orderByChild("count");
        reference[2] = mDatabase.getReference().child("form").orderByChild("today");

        /* 전체보기 버튼 */
        btn_c[0] = (TextView) root.findViewById(R.id.btn_ctgy0);
        btn_c[1] = (TextView) root.findViewById(R.id.btn_ctgy1);
        btn_c[2] = (TextView) root.findViewById(R.id.btn_ctgy2);
        btn_c[3] = (TextView) root.findViewById(R.id.btn_ctgy3);


        /* 각 listview에 상세 페이지 이동 연동 */
        for(int i=0;i<4;i++){
            homelist[i]=new ArrayList<>();
            homelistAdapter[i] = new homelist_adapter(homelist[i]);
            int finalI = i;
            homelistAdapter[i].setOnItemClickListener(new homelist_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    String FID   = homelist[finalI].get(position).getFID();
                    String UID   = homelist[finalI].get(position).getUID();
                    //인텐트 선언 및 정의
                    Intent intent = new Intent(getContext(), FormdetailActivity.class);
                    //입력한 input값을 intent로 전달한다.
                    intent.putExtra("FID", FID);
                    intent.putExtra(("UID_dash"),UID);
                    //액티비티 이동
                    startActivity(intent);
                }
            });
        }

        /* 각 레퍼런스 데이터 불러오기 */
        GetOrderByreference(reference[0],0);
        GetOrderByreference(reference[1],1);
        GetOrderByreference(reference[2],2);
        GetMyReference(3);
        /* 전체보기 버튼 클릭 시 CategoryActivity 이동 설정 */
        for(int i=0;i<4;i++){
            btn_c[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Retrofit_Function.category();
                    Intent intent = new Intent(getActivity(), CategoryActivity.class);
                    startActivity(intent);
                }
            });
        }

        /* 상단의 툴바 검색 버튼 설정 */
        root.findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        /* 상단의 툴바 알림 버튼 설정 */
        root.findViewById(R.id.notification_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationsActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    /* Adapter에 넣을 Data 입력 */
    public void InitializeFormData(int i, String FID, String img, String title, String UID,
                                   String mans, String category,String location, String cost)
    {
        homelist_data tmp = new homelist_data();

        Resources res = getResources();
        String[] cat = res.getStringArray(R.array.category);
        category=cat[Integer.parseInt(category)];
        tmp.setImgName(img);
        tmp.setTitle(title);
        tmp.setCost(cost);
        tmp.setMans(mans);
        tmp.setFID(FID);
        tmp.setUID(UID);
        tmp.setCategory(category);
        tmp.setLocation(location);
        tmp.setCost(cost);
        homelist[i].add(tmp);

    }
    /* 마감/인기/신규순 리스트 출력 */
    public void GetOrderByreference(Query reference, int i){
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot result = task.getResult();
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else{
                    homelist[i].clear();
                    int count = 0;
                    for (DataSnapshot fileSnapshot : result.getChildren() ) {
                        if(count<10 && fileSnapshot.child("active").getValue().toString().equals("0")
                                    && fileSnapshot.child("state").getValue().toString().equals("0")) {
                            Log.e(""+i,fileSnapshot.child("active").getValue().toString());
                            String Key = fileSnapshot.getKey();
                            String subject = fileSnapshot.child("subject").getValue().toString();
                            String max_count = fileSnapshot.child("max_count").getValue().toString();
                            String UID = fileSnapshot.child("UID_dash").getValue().toString();
                            String parti_num = fileSnapshot.child("parti_num").getValue().toString();
                            String image = fileSnapshot.child("image").getValue().toString();
                            String category = fileSnapshot.child("category_text").getValue().toString();
                            String location = fileSnapshot.child("address").getValue().toString();
                            String cost = fileSnapshot.child("cost").getValue().toString();
                            InitializeFormData(i, Key, image, subject, UID, parti_num + "/" + max_count, category, location, cost);
                            count++;
                        }
                    }
                }
                recyclerView[i].setAdapter(homelistAdapter[i]);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                if(i>0){
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                }
                recyclerView[i].setLayoutManager(linearLayoutManager);
            }
        });
    }
    /*관심카테고리 출력*/
    public void GetMyReference( int i){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int x=2;x<my_list.length;x++){
            if(my_list[x]) list.add(x);
        }
        Log.e("my_List",list.size()+" "+list);
        Query reference = mDatabase.getReference().child("form").orderByChild("active").endAt(0);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    homelist[i].clear();
                    DataSnapshot result = task.getResult();
                    int count=0;
                    if(list.isEmpty()){
                        for (DataSnapshot fileSnapshot : result.getChildren() ) {
                            if(count<10 && fileSnapshot.child("state").getValue().toString().equals("0")){
                                String Key       = fileSnapshot.getKey();
                                String subject   = fileSnapshot.child("subject").getValue().toString();
                                String max_count = fileSnapshot.child("max_count").getValue().toString();
                                String UID       = fileSnapshot.child("UID_dash").getValue().toString();
                                String parti_num = fileSnapshot.child("parti_num").getValue().toString();
                                String image     = fileSnapshot.child("image").getValue().toString();
                                String category  = fileSnapshot.child("category_text").getValue().toString();
                                String location  = fileSnapshot.child("address").getValue().toString();
                                String cost      = fileSnapshot.child("cost").getValue().toString();
                                InitializeFormData(i,Key,image,subject,UID,parti_num+"/"+max_count,category, location, cost);
                                count++;
                            }
                        }
                    }else{
                        for (DataSnapshot fileSnapshot : result.getChildren() ) {

                            String category = fileSnapshot.child("category_text").getValue().toString();
                            int temp = Integer.parseInt(category);
                            if(list.contains(temp)){
                                if(count<10 && fileSnapshot.child("state").getValue().toString().equals("0")){
                                    String Key       = fileSnapshot.getKey();
                                    String subject   = fileSnapshot.child("subject").getValue().toString();
                                    String max_count = fileSnapshot.child("max_count").getValue().toString();
                                    String UID       = fileSnapshot.child("UID_dash").getValue().toString();
                                    String parti_num = fileSnapshot.child("parti_num").getValue().toString();
                                    String image     = fileSnapshot.child("image").getValue().toString();
                                    String location  = fileSnapshot.child("address").getValue().toString();
                                    String cost      = fileSnapshot.child("cost").getValue().toString();
                                    InitializeFormData(i,Key,image,subject,UID,parti_num+"/"+max_count,category, location, cost);
                                    count++;
                                }
                            }
                        }
                    }
                }
                recyclerView[i].setAdapter(homelistAdapter[i]);
                recyclerView[i].setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    /* mylist 배열 초기화 */
    public void initmylist(){
        for(int i=0;i<15;i++){
            my_list[i]=false;
        }
        mDatabase.getReference().child("users").child(currentUser.getUid()).child("mycategory").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    DataSnapshot result = task.getResult();
                    if(result!=null){
                        for (DataSnapshot fileSnapshot : result.getChildren() ) {
                            String temp = fileSnapshot.getValue().toString();
                            int x = Integer.parseInt(temp);
                            my_list[x]=true;
                        }
                    }
                    setmycategory();
                    GetMyReference(3);
                }
            }
        });
    }
    /* mylist에 true값을 가진 요소로 카테고리 태그 출력*/
    public void setmycategory(){
        categoryAdapter_my.isEmpty();
        Resources res = getResources();
        String[] cat = res.getStringArray(R.array.category);
        gridViews.removeAllViewsInLayout();
        categoryAdapter_my.clear();
        int x = 0;
        for (int i=0;i< cat.length;i++) {
            String name = cat[i];
            if(my_list[i]){
                categoryAdapter_my.addItem(new CategoryData(i+"",name));
            }
        }
        categoryAdapter_my.notifyDataSetChanged();
    }
}