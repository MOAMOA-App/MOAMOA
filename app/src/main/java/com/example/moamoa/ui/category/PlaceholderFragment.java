package com.example.moamoa.ui.category;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
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

    private PageViewModel pageViewModel;
    private EmptyFormsBinding binding;  //empty_forms를 viewpager에 binding
    FirebaseDatabase firebaseDatabase;

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

//        binding = FragmentMainBinding.inflate(inflater, container, false);
        binding = EmptyFormsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //DatabaseReference database = firebaseDatabase.getInstance().getReference();
        ListView listView;
        listView = root.findViewById(R.id.listview);
        ArrayList<Form> listViewData = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Form listData = snapshot.getValue(Form.class);
                    Log.i(TAG, "onDataChange: "+listData);
                        //listData.subject =listData.subject;
                    Log.d("FbDatabase", snapshot.toString());

                    //listData.subject =listData.subject;
                        //listData.subject = listData.getSubject();
                    //listData.photo = R.drawable.ic_launcher_background;
                    listViewData.add(listData);
                    ListAdapter oAdapter = new CustomListView(listViewData);
                    listView.setAdapter(oAdapter);
                    //Log.d("MainActivity", "ValueEventListener : " + listData.subject );
                    //Log.d("MainActivity", "ValueEventListener : " + snapshot.getValue(Form.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
            });
        //추가

        /*for (int i=0; i<30; ++i) {
            Form listData = new Form();
            listData.photo = R.drawable.ic_launcher_background;
            listData.subject = "테스트"+i;
            listData.text = "화성";
            listData.cost = String.valueOf(i);
            listData.deadline= String.valueOf(i+2);
            listViewData.add(listData);
        }
*/
        //viewpager에 리스트 띄워줌


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

