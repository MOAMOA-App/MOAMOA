package com.example.moamoa.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;
    private GridView[] gridViews = new GridView[2];
    private CategoryAdapter categoryAdapter;
    private CategoryAdapter_my categoryAdapter_my;
    public static CategoryFragment mContext;
    TextView btn_edit;
    private List<String> name_list = new ArrayList<>();
    private List<Integer> numb_list = new ArrayList<>();
    boolean[] my_list = new boolean[15];

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = this;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btn_edit = (TextView) root.findViewById(R.id.btn_edit);
        gridViews[0] = (GridView) root.findViewById(R.id.my_category);
        gridViews[1] = (GridView) root.findViewById(R.id.category_layout);
        categoryAdapter = new CategoryAdapter();
        categoryAdapter_my = new CategoryAdapter_my();
        MyAlertDialogFragment myAlertDialogFragment
                = MyAlertDialogFragment.newInstance("관심 카테고리 편집");

        my_list=initmylist();


        gridViews[0].setAdapter(categoryAdapter_my);
        //카테고리 grid 출력
        mDatabase.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //변화된 값이 DataSnapshot 으로 넘어온다.
                //데이터가 쌓이기 때문에  clear()
                int x = 0;
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String numb = x + "";
                    String name = fileSnapshot.getValue().toString();
                    categoryAdapter.addItem(new CategoryData(numb, name));
                    if(x>1){
                        name_list.add(name);
                    }
                    x++;
                }
                myAlertDialogFragment.set_list(name_list);

                gridViews[1].setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        mDatabase.child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                }
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAlertDialogFragment.show(getFragmentManager(), "dialog");
            }
        });

        return root;
    }
    public void setmycategory(boolean[] list){
        mDatabase.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //변화된 값이 DataSnapshot 으로 넘어온다.
                //데이터가 쌓이기 때문에  clear()
                categoryAdapter_my.isEmpty();
                int x = 0;
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String numb = x + "";
                    String name = fileSnapshot.getValue().toString();
                    if(list[x]){
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
    public boolean[] initmylist(){
        boolean[] my_list = new boolean[15];
        for(int i=0;i<15;i++){
            my_list[i]=false;
        }
        mDatabase.child("users").child(currentUser.getUid()).child("mycategory").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                        setmycategory(my_list);
                    }

                }
            }
        });
        return my_list;
    }



    public static class MyAlertDialogFragment extends DialogFragment {
        private DatabaseReference mDatabase;
        private List<String> list = new ArrayList<>();
        private boolean[] my_list;

        public static MyAlertDialogFragment newInstance(String title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            frag.setArguments(args);
            return frag;
        }
        public void set_list(List list){
            this.list = list;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final CharSequence[] items = list.toArray(new String[list.size()]);
            List choices = new ArrayList();
            choices.clear();
            final boolean[] checkedItems = new boolean[list.size()];

            String title = getArguments().getString("title");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMultiChoiceItems(items, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    if (b) {
                                        // 체크 됐으면 리스트에 추가
                                        //           Log.d("확인","name : "+checkedItems[i]);
                                        choices.add(i+2);

                                        //       checkedItems[i] = true;
                                        //               Log.d("확인","name : "+checkedItems[i]);
                                    } else if (choices.contains(i+2)) {
                                        // 체크 된거면 리스트에서 제거
                                        choices.remove(Integer.valueOf(i+2));

                                    }
                                }
                            })
                    .setPositiveButton("완료",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {
                                    HashMap<String,Object> childUpdates = new HashMap<>();
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("users");
                                    childUpdates.put("mycategory", choices);
                                    reference.child(currentUser.getUid()).updateChildren(childUpdates);
                                    my_list=((CategoryFragment)CategoryFragment.mContext).initmylist();
                                    /*
                                    for (int k = 0; k < 7; k++) {
                                        getActivity().findViewById(Rid_button[k]).setVisibility(View.GONE);
                                    }

                                    if (choices.size() > 0) {
                                        String cities = "";

                                        for (int j = 0; j < choices.size(); j++) {
                                            int position = (int) choices.get(j);
                                            cities = cities + " " + list.get(position);

                                            button_heart[j].setText(list.get(position));
                                            getActivity().findViewById(Rid_button[j]).setVisibility(View.VISIBLE);

                                        }
                                        Toast.makeText(getActivity(),
                                                cities + "\n이상 " + choices.size() + " 카테고리 선택됨", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                    */
                                }
                            })
                    .setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {

                                }
                            })
                    .show();
        }
    }
}

