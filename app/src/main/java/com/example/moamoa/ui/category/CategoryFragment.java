package com.example.moamoa.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentCategoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private DatabaseReference mDatabase;
    private GridView[] gridViews = new GridView[2];
    private CategoryAdapter categoryAdapter;
    TextView btn_edit;
    static Button[] button_heart = new Button[7];

    static Integer[] Rid_button = {

            R.id.all_group1, R.id.my_group1, R.id.food_group1, R.id.dailyitems_group1, R.id.clothes_group1,

            R.id.appliance_group1, R.id.game_group1

    };
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_edit = (TextView) root.findViewById(R.id.btn_edit);
        gridViews[1] = (GridView) root.findViewById(R.id.category_layout);
        categoryAdapter = new CategoryAdapter();
        mDatabase.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //변화된 값이 DataSnapshot 으로 넘어온다.
                //데이터가 쌓이기 때문에  clear()
                int x = 0;
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String numb = x + "";
                    String name = fileSnapshot.getValue().toString();
                    categoryAdapter.addItem(new CategoryData(numb, name));

                    x++;
                }
                gridViews[1].setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        button_heart[0] = (Button) root.findViewById(R.id.all_group1);
        button_heart[1] = (Button) root.findViewById(R.id.my_group1);
        button_heart[2] = (Button) root.findViewById(R.id.food_group1);
        button_heart[3] = (Button) root.findViewById(R.id.dailyitems_group1);
        button_heart[4] = (Button) root.findViewById(R.id.clothes_group1);
        button_heart[5] = (Button) root.findViewById(R.id.appliance_group1);
        button_heart[6] = (Button) root.findViewById(R.id.game_group1);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAlertDialogFragment newDialogFragment
                        = MyAlertDialogFragment.newInstance("관심 카테고리 편집");
                newDialogFragment.show(getFragmentManager(), "dialog");
            }
        });

        return root;
    }

    public static class MyAlertDialogFragment extends DialogFragment {
        private DatabaseReference mDatabase;
        public static MyAlertDialogFragment newInstance(String title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            frag.setArguments(args);
            return frag;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //return super.onCreateDialog(savedInstanceState);

            List<String> list = new ArrayList<>();

            list.add("식품");
            list.add("의류");
            list.add("생활용품");
            list.add("취미");
            list.add("기타");


            final CharSequence[] items = list.toArray(new String[list.size()]);
            List choices = new ArrayList();
            choices.clear();

            final boolean[] checkedItems = new boolean[]{ false, false, false, false, false};

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
                                        choices.add(i);

                                        //       checkedItems[i] = true;
                                        //               Log.d("확인","name : "+checkedItems[i]);
                                    } else if (choices.contains(i)) {
                                        // 체크 된거면 리스트에서 제거
                                        choices.remove(Integer.valueOf(i));

                                    }
                                }
                            })
                    .setPositiveButton("완료",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {
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

