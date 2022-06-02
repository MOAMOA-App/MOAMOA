package com.example.moamoa.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentHomeBinding;
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
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        homelist = new ArrayList<>();
        homelistAdapter = new homelist_adapter(homelist);

        recyclerView = (RecyclerView) root.findViewById(R.id.listview1);

        mDatabase.child("Forms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //변화된 값이 DataSnapshot 으로 넘어온다.
                //데이터가 쌓이기 때문에  clear()
                int x=0;
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String Key = fileSnapshot.getKey();
                    String subjecttext = dataSnapshot.child("subject").getValue(String.class);
                    String maxtext = dataSnapshot.child("max_count").getValue(String.class);
                    InitializeFormData("",subjecttext,Key,maxtext,Key);
                    x++;
                    if(x>10){ break;}
                }
                recyclerView.setAdapter(homelistAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        homelistAdapter.setOnItemClickListener(new homelist_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String FID = homelist.get (position).getFID();
                //인텐트 선언 및 정의
                Intent intent = new Intent(getContext(), FormdetailActivity.class);
                //입력한 input값을 intent로 전달한다.
                intent.putExtra("FID", FID);
                //액티비티 이동
                startActivity(intent);
                //Toast.makeText (getContext(), "FID : "+FID, Toast.LENGTH_SHORT).show ();
            }
        });
        return root;
    }

    public void InitializeFormData(String img, String title, String name, String mans, String FID)
    {
        homelist_data tmp = new homelist_data();
        tmp.setImgName(img);
        tmp.setTitle(title);
        tmp.setName(name);
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