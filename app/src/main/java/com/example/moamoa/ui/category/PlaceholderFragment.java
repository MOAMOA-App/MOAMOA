package com.example.moamoa.ui.category;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.EmptyFormsBinding;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private int index = 1;

    private PageViewModel pageViewModel;
    private EmptyFormsBinding binding;  //empty_forms를 viewpager에 binding
    FirebaseDatabase firebaseDatabase;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        Log.d("확인","첫번째 : "+fragment);
        fragment.setArguments(bundle);
        Log.d("확인","두번째 : "+bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        //int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);


        }

        pageViewModel.setIndex(index);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        //Log.d("FbDatabase인덱스", String.valueOf(index));
//        binding = FragmentMainBinding.inflate(inflater, container, false);
        int pos = getArguments().getInt(ARG_SECTION_NUMBER);


        binding = EmptyFormsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        //index = bundle.getInt("grade", 0);
        //DatabaseReference database = firebaseDatabase.getInstance().getReference();
        Log.d("확인","컨텐 : "+container);
        Log.d("확인","바인당 : "+binding);
        Log.d("확인","루트 : "+pos);

        ListView listView;
        listView = root.findViewById(R.id.listview);
        ArrayList<Form> listViewData = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Form listData = snapshot.getValue(Form.class);

                    //v

                    if ( pos==1){

                        listViewData.add(listData);
                    }

                    if (listData.category_text.equals("식품") && pos==3 && listData.getstate()==0){

                        listViewData.add(listData);
                    }
                    if (listData.category_text.equals("의류") && pos==4 && listData.getstate()==0){

                        listViewData.add(listData);
                    }
                    if (listData.category_text.equals("생활용품") && pos==5 && listData.getstate()==0){

                        listViewData.add(listData);
                    }
                    if (listData.category_text.equals("취미") && pos==6 && listData.getstate()==0){

                        listViewData.add(listData);
                    }
                    if (listData.category_text.equals("기타") && pos==7 && listData.getstate()==0){

                        listViewData.add(listData);
                    }


                    ListAdapter oAdapter = new CustomListView(listViewData);
                    listView.setAdapter(oAdapter);


                }
                index=1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
            });
        //추가


        //viewpager에 리스트 띄워줌


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("확인","message : "+"");
                String FID = listViewData.get(position).FID;
                String title = listViewData.get(position).subject;
                Log.d("확인","message : "+FID);
                Log.d("확인","message : "+title);
                //인텐트 선언 및 정의


                Intent intent = new Intent(getContext(), FormdetailActivity.class);
                //입력한 input값을 intent로 전달한다.
                intent.putExtra("FID", FID);
                //액티비티 이동
                startActivity(intent);
              //Toast.makeText (getContext(), "FID : "+FID, Toast.LENGTH_SHORT).show ();
            }
        });
//        homelistAdapter.setOnItemClickListener(new homelist_adapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                String FID = homelist.get(position).getFID();
//                String title = homelist.get(position).getTitle();
//                //인텐트 선언 및 정의
//                Intent intent = new Intent(getContext(), FormdetailActivity.class);
//                //입력한 input값을 intent로 전달한다.
//                intent.putExtra("FID", FID);
//                //액티비티 이동
//                startActivity(intent);
//                //Toast.makeText (getContext(), "FID : "+FID, Toast.LENGTH_SHORT).show ();
//            }
//        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

