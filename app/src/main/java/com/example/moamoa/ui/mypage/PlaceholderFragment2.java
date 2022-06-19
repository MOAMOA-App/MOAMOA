package com.example.moamoa.ui.mypage;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment2 extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    int name;
    private PageViewModel pageViewModel;
    private EmptyFormsBinding binding;  //empty_forms를 viewpager에 binding
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Form listData;
    public static PlaceholderFragment2 newInstance(int index) {
        PlaceholderFragment2 fragment = new PlaceholderFragment2();
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

        //추가
        ListView listView;
        listView = root.findViewById(R.id.listview);
        ArrayList<Form> listViewData = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {


                FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listViewData.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            listData = snapshot.getValue(Form.class);


                            if (dataSnapshot2.child(user.getUid()).child(listData.FID).getValue()!=null) {
                                if (dataSnapshot2.child(user.getUid()).child(listData.FID).getValue().toString().equals("parti") && listData.getstate() == 0 && pos == 1) {

                                    listViewData.add(listData);
                                }
                                if (name == 1 && listData.getstate() == 1 && pos == 2) {

                                    listViewData.add(listData);
                                }

                                if (name == 1 && listData.getstate() == 2 && pos == 3) {

                                    listViewData.add(listData);
                                }
                            }

                            //viewpager에 리스트 띄워줌
                            ListAdapter oAdapter = new CustomListView(listViewData);
                            listView.setAdapter(oAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickName = listViewData.get(position).subject;
                Log.d("확인","name : "+clickName);
            }
        });
//

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
//

