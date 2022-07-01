package com.example.moamoa.ui.home;

import android.content.Intent;
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
import com.example.moamoa.databinding.FragmentHomeBinding;
import com.example.moamoa.ui.category.CategoryActivity;
import com.example.moamoa.ui.category.CategoryAdapter_my;
import com.example.moamoa.ui.category.CategoryData;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<homelist_data> homelist[]=new ArrayList[5];
    private homelist_adapter homelistAdapter[]=new homelist_adapter[5];
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

        categoryAdapter_my = new CategoryAdapter_my();
        gridViews = (GridView) root.findViewById(R.id.home_my_category);
        my_list=initmylist();
        //카테고리 grid 출력

        gridViews.setAdapter(categoryAdapter_my);
        mDatabase.getReference().child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //변화된 값이 DataSnapshot 으로 넘어온다.
                //데이터가 쌓이기 때문에  clear()
                int x = 0;
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String numb = x + "";
                    String name = fileSnapshot.getValue().toString();
                    if(my_list[x]){
                        categoryAdapter_my.addItem(new CategoryData(numb,name));
                    }
                    x++;
                }
                gridViews.setAdapter(categoryAdapter_my);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        recyclerView[0] = (RecyclerView) root.findViewById(R.id.listview0);
        recyclerView[1] = (RecyclerView) root.findViewById(R.id.listview1);
        recyclerView[2] = (RecyclerView) root.findViewById(R.id.listview2);
        recyclerView[3] = (RecyclerView) root.findViewById(R.id.listview3);
        recyclerView[4] = (RecyclerView) root.findViewById(R.id.listview4);
        btn_c[0] = (TextView) root.findViewById(R.id.btn_ctgy0);
        btn_c[1] = (TextView) root.findViewById(R.id.btn_ctgy1);
        btn_c[2] = (TextView) root.findViewById(R.id.btn_ctgy2);
        btn_c[3] = (TextView) root.findViewById(R.id.btn_ctgy3);    // 관심카테고리
        btn_c[4] = (TextView) root.findViewById(R.id.btn_ctgy4);

        reference[0] = mDatabase.getReference().child("form").orderByChild("deadline").limitToFirst(10);    //마감임박
        reference[1] = mDatabase.getReference().child("form").orderByChild("parti_num").limitToLast(10);   //인기별
        reference[2] = mDatabase.getReference().child("form").orderByChild("today").limitToFirst(10);       //신규
        reference[2] = mDatabase.getReference().child("form").orderByChild("today").limitToFirst(10);       //나의 관심 카테고리
        reference[2] = mDatabase.getReference().child("form").orderByChild("today").limitToFirst(10);       //최근 본 게시글
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        for(int i=0;i<5;i++){
            homelist[i]=new ArrayList<>();
            homelistAdapter[i] = new homelist_adapter(homelist[i]);
            int finalI = i;
            homelistAdapter[i].setOnItemClickListener(new homelist_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    String FID = homelist[finalI].get(position).getFID();
                    String title = homelist[finalI].get(position).getTitle();
                    //인텐트 선언 및 정의
                    Intent intent = new Intent(getContext(), FormdetailActivity.class);
                    //입력한 input값을 intent로 전달한다.
                    intent.putExtra("FID", FID);
                    //액티비티 이동
                    startActivity(intent);
                    //Toast.makeText (getContext(), "FID : "+FID, Toast.LENGTH_SHORT).show ();
                }
            });
        }

        setOrderBydeadline(reference[0],0);
        setOrderBydeadline(reference[1],1);
        setOrderBydeadline(reference[2],2);


        for(int i=0;i<5;i++){
            btn_c[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CategoryActivity.class);
                    startActivity(intent);
                }
            });
        }
        return root;
    }
    public void InitializeFormData(int i,String img, String title, String UID, String mans, String FID)
    {
        homelist_data tmp = new homelist_data();
        tmp.setImgName(img);
        tmp.setTitle(title);
        tmp.setNick(UID);
        tmp.setMans(mans);
        tmp.setFID(FID);
        homelist[i].add(tmp);

    }
    public void setOrderBydeadline(Query reference, int i){
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    homelist[i].clear();
                    DataSnapshot result = task.getResult();
                    for (DataSnapshot fileSnapshot : result.getChildren() ) {
                        String Key = fileSnapshot.getKey();
                        String subject = fileSnapshot.child("today").getValue().toString().substring(4,8)+"_"+fileSnapshot.child("deadline").getValue().toString().substring(4);

                        //String subject = fileSnapshot.child("subject").getValue().toString();
                        String max_count = fileSnapshot.child("max_count").getValue().toString();
                        String UID = fileSnapshot.child("UID_dash").getValue().toString();
                        String parti_num = fileSnapshot.child("parti_num").getValue().toString();
                        String image =  fileSnapshot.child("image").getValue().toString();
                        InitializeFormData(i,image,subject,UID,parti_num+"/"+max_count,Key);
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

    public boolean[] initmylist(){
        boolean[] my_list = new boolean[15];
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
                    //Log.e("firebase", result.toString());
                    if(result!=null){
                        for (DataSnapshot fileSnapshot : result.getChildren() ) {
                            //my_list[(int) fileSnapshot.getValue()]=true;
                            String temp = fileSnapshot.getValue().toString();
                            int x = Integer.parseInt(temp);
                            my_list[x]=true;
                        }
                        setmycategory();
                    }

                }
            }
        });
        return my_list;
    }

    public void setmycategory(){
        categoryAdapter_my.isEmpty();
        mDatabase.getReference().child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //변화된 값이 DataSnapshot 으로 넘어온다.
                //데이터가 쌓이기 때문에  clear()

                int x = 0;
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String numb = x + "";
                    String name = fileSnapshot.getValue().toString();
                    if(my_list[x]){
                        categoryAdapter_my.addItem(new CategoryData(numb,name));
                    }
                    x++;
                }
                categoryAdapter_my.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}