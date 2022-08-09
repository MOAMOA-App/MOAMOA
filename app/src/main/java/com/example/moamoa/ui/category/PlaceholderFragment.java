package com.example.moamoa.ui.category;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private EmptyFormsBinding binding;  //empty_forms를 viewpager에 binding
    int[] ca_num;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
        int pos = getArguments().getInt(ARG_SECTION_NUMBER);

//        binding = FragmentMainBinding.inflate(inflater, container, false);
        binding = EmptyFormsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
ca_num = new int[8];
List<Integer> ca_num = new ArrayList();
        //추가
        ListView listView;
        listView = root.findViewById(R.id.listview);
        ArrayList<Form> listViewData = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("User").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listViewData.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    CategoryData userData = snapshot.getValue(CategoryData.class);
                    Log.d("확인","마이 카테고리 : "+userData.getName());
                    ca_num.add(Integer.parseInt(userData.getName()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listViewData.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Form listData = snapshot.getValue(Form.class);
                    Log.d("확인","루트 : "+user.getUid());



                    if ( pos==1){

                        listViewData.add(listData);
                    }
                    if ( pos==2){
                        for (int i = 0; i < 8; i++) {
                          //  if (ca_num.equals(listData.category_text)){
                                listViewData.add(listData);
                           // }
                        }

//                        listViewData.add(listData);
                    }

                    if (listData.category_text==2 && pos==3 && listData.getstate()==0){
//식품
                        listViewData.add(listData);
                    }
                    if (listData.category_text==5 && pos==4 && listData.getstate()==0){
//의류
                        listViewData.add(listData);
                    }
                    if (listData.category_text==3 && pos==5 && listData.getstate()==0){
//생활
                        listViewData.add(listData);
                    }
                    if (listData.category_text==6 && pos==6 && listData.getstate()==0){
//취미
                        listViewData.add(listData);
                    }
                    if (listData.category_text==7 && pos==7 && listData.getstate()==0){
//기타
                        listViewData.add(listData);
                    }


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
}
//

