package com.example.moamoa.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentHomeBinding;
import com.example.moamoa.ui.category.CategoryActivity;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ArrayList<homelist_data> homelist;
    private homelist_adapter homelistAdapter;
    private RecyclerView[] recyclerView = new RecyclerView[5];
    private DatabaseReference mDatabase;
    private TextView[] btn_c = new TextView[5];
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        homelist = new ArrayList<>();
        homelistAdapter = new homelist_adapter(homelist);

        recyclerView[0] = (RecyclerView) root.findViewById(R.id.listview0);
        recyclerView[1] = (RecyclerView) root.findViewById(R.id.listview1);
        recyclerView[2] = (RecyclerView) root.findViewById(R.id.listview2);
        recyclerView[3] = (RecyclerView) root.findViewById(R.id.listview3);
        recyclerView[4] = (RecyclerView) root.findViewById(R.id.listview4);


        mDatabase.child("form").limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //변화된 값이 DataSnapshot 으로 넘어온다.
                //데이터가 쌓이기 때문에  clear()
                int x=0;
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {

                    if (Integer.parseInt(fileSnapshot.child("state").getValue().toString())==0)
                    { String Key = fileSnapshot.getKey();
                    String subject = fileSnapshot.child("subject").getValue().toString();
                    String max_count = fileSnapshot.child("max_count").getValue().toString();
                    String UID = fileSnapshot.child("UID_dash").getValue().toString();
                    String parti_num = fileSnapshot.child("parti_num").getValue().toString();
                    String image =  fileSnapshot.child("image").getValue().toString();
                    InitializeFormData(image,subject,UID,parti_num+"/"+max_count,Key);
                    x++;}
                    if(x>10){ break;}

                }
                for(int i=0;i<5;i++){
                    recyclerView[i].setAdapter(homelistAdapter);
                    recyclerView[i].setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        homelistAdapter.setOnItemClickListener(new homelist_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String FID = homelist.get(position).getFID();
                String title = homelist.get(position).getTitle();
                //인텐트 선언 및 정의
                Intent intent = new Intent(getContext(), FormdetailActivity.class);
                //입력한 input값을 intent로 전달한다.
                intent.putExtra("FID", FID);
                //액티비티 이동
                startActivity(intent);
                //Toast.makeText (getContext(), "FID : "+FID, Toast.LENGTH_SHORT).show ();
            }
        });

        btn_c[0] = (TextView) root.findViewById(R.id.btn_ctgy0);
        btn_c[1] = (TextView) root.findViewById(R.id.btn_ctgy1);
        btn_c[2] = (TextView) root.findViewById(R.id.btn_ctgy2);
        btn_c[3] = (TextView) root.findViewById(R.id.btn_ctgy3);
        btn_c[4] = (TextView) root.findViewById(R.id.btn_ctgy4);
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

    public void InitializeFormData(String img, String title, String UID, String mans, String FID)
    {
        homelist_data tmp = new homelist_data();
        tmp.setImgName(img);
        tmp.setTitle(title);
        tmp.setNick(UID);
        tmp.setMans(mans);
        tmp.setFID(FID);
        homelist.add(tmp);

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}