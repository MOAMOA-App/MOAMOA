package com.example.moamoa.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
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
import java.util.Arrays;
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
        Resources res = getResources();
        String[] name_list = res.getStringArray(R.array.category);

        gridViews[0].setAdapter(categoryAdapter_my);
        gridViews[1].setAdapter(categoryAdapter);
        for (int i=0;i<name_list.length;i++) {
            String numb = i + "";
            String name = name_list[i];
            categoryAdapter.addItem(new CategoryData(numb, name));
        }

        name_list=Arrays.copyOfRange(name_list,2,name_list.length);
        List mycatname_list = Arrays.asList(name_list);
        myAlertDialogFragment.set_list(mycatname_list);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAlertDialogFragment.show(getFragmentManager(), "dialog");
            }
        });

        return root;
    }
    public void setmycategory(boolean[] list){
        gridViews[0].removeAllViewsInLayout();
        categoryAdapter_my.clear();

        Resources res = getResources();
        String[] name_list = res.getStringArray(R.array.category);
        for (int i=0;i< name_list.length;i++) {
            String name = name_list[i];
            if(my_list[i]){
                categoryAdapter_my.addItem(new CategoryData(i+"",name));
            }
        }
        categoryAdapter_my.notifyDataSetChanged();
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
                    if(result!=null){
                        for (DataSnapshot fileSnapshot : result.getChildren() ) {
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
        private List<String> list = new ArrayList<>();

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
                                        choices.add(i+2);

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
                                    ((CategoryFragment)CategoryFragment.mContext).initmylist();
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

