package com.example.moamoa.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    Button btn_edit;
    private FragmentCategoryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btn_edit = root.findViewById(R.id.btn_edit);
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

        public static MyAlertDialogFragment newInstance(String title){
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
            list.add("생활");
            list.add("의류");
            list.add("가전");
            list.add("게임/취미");
            list.add("스포츠");
            list.add("굿즈");
            list.add("반려동물");
            list.add("기타 잡화");

            final CharSequence[] items = list.toArray(new String[list.size()]);
            final List choices = new ArrayList();


            String title = getArguments().getString("title");
            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMultiChoiceItems(items, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    if (b) {
                                        // 체크 됐으면 리스트에 추가
                                        choices.add(i);
                                    } else if (choices.contains(i)) {
                                        // 체크 된거면 리스트에서 제거
                                        choices.remove(Integer.valueOf(i));
                                    }
                                }
                            })
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("MyLog", "확인 버튼이 눌림");
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("MyLog", "취소 버튼이 눌림");
                        }
                    })
                    .create();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}