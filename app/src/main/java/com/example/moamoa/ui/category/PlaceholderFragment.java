package com.example.moamoa.ui.category;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.CreatedFormsBinding;
import com.example.moamoa.databinding.EmptyFormsBinding;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * A placeholder fragment containing a simple view.
 */

/**
 *
 */

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private DatabaseReference mDatabase;
    private PageViewModel pageViewModel;
    private EmptyFormsBinding binding;  //empty_forms를 viewpager에 binding
    int[] ca_num;
    ArrayList<Form> listViewData = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Form  listData;
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        //탭번호
        int pos = getArguments().getInt(ARG_SECTION_NUMBER);

        binding = EmptyFormsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ca_num = new int[8];
        List<Integer> ca_num = new ArrayList();
        //추가
        ListView listView;
        listView = root.findViewById(R.id.listview);
        Button button_dead = (Button) root.findViewById(R.id.sort_dead);    //마감순
        Button button_hot = (Button) root.findViewById(R.id.sort_hot);      //인기순
        Button button_new = (Button) root.findViewById(R.id.sort_new);      //최신순
        button_dead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_dead.setSelected(true);
                button_hot.setSelected(false);
                button_new.setSelected(false);
                listData.s_case = 1;
                listViewData.add(listData);
                Collections.sort(listViewData);
                ListAdapter oAdapter = new CustomListView(listViewData);
                listView.setAdapter(oAdapter);

            }
        });
        button_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_dead.setSelected(false);
                button_hot.setSelected(true);
                button_new.setSelected(false);
                listData.s_case = 0;
                listViewData.add(listData);
                Collections.sort(listViewData);
                ListAdapter oAdapter = new CustomListView(listViewData);
                listView.setAdapter(oAdapter);
            }
        });
        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_dead.setSelected(false);
                button_hot.setSelected(false);
                button_new.setSelected(true);

            }
        });

        FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listViewData.clear();

                Resources res = getResources();
                String[] category_list = res.getStringArray(R.array.category);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    listData = snapshot.getValue(Form.class);
                    Log.d("MainActivity", "ChildEventListener -  : " + listData);
                    switch(pos){
                        case 1:
                            listViewData.add(listData);
                            break;
                        case 2:
                            for (int i = 0; i < 2; i++) {
                                listViewData.add(listData);
                            }
                            break;
                        default:
                            if(listData.category_text==pos-1 && listData.getstate()==0){
                                listViewData.add(listData);
                            }
                    }
                    /*
                    if ( pos==1 ){
                    }
                    if ( pos==2){
                        for (int i = 0; i < 2; i++) {
                            listViewData.add(listData);
                        }
                    }
                    for(int i=2;i<category_list.length;i++){
                        if (listData.category_text==2 && pos==3 ){
//식품
                            listViewData.add(listData);
                        }
                    }

                    if (listData.category_text==5 && pos==4 ){
//의류
                        listViewData.add(listData);
                    }
                    if (listData.category_text==3 && pos==5){
//생활
                        listViewData.add(listData);
                    }
                    if (listData.category_text==6 && pos==6 ){
//취미
                        listViewData.add(listData);
                    }
                    if (listData.category_text==7 && pos==7 ){
//기타
                        listViewData.add(listData);
                    }
                    if (button_hot.isSelected()){

                    }

                     */
                    ListAdapter oAdapter = new CustomListView(listViewData);
                    listView.setAdapter(oAdapter);


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String FID = listViewData.get(position).FID;
                String title = listViewData.get(position).subject;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void UserFind(String UID){


    }


        }

//

