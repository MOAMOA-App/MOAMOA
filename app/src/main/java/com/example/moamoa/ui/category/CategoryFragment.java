package com.example.moamoa.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.moamoa.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    Button btn_edit;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button button10;
    Button button11;

    CategoryActivity categoryActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);

        btn_edit =rootView.findViewById(R.id.btn_edit);
        button1 = rootView.findViewById(R.id.all_group);
        button2 = rootView.findViewById(R.id.my_group);
        button3 = rootView.findViewById(R.id.food_group);
        button4 = rootView.findViewById(R.id.dailyitems_group);
        button5 = rootView.findViewById(R.id.clothes_group);
        button6 = rootView.findViewById(R.id.appliance_group);
        button7 = rootView.findViewById(R.id.game_group);

        // 프래그먼트 1에서 프래그먼트 2를 띄운다.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ListupActivity.class);
                intent.putExtra("tabIdx", 0);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ListupActivity.class);
                intent.putExtra("tabIdx", 1);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ListupActivity.class);
                intent.putExtra("tabIdx", 2);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ListupActivity.class);
                intent.putExtra("tabIdx", 3);
                startActivity(intent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ListupActivity.class);
                intent.putExtra("tabIdx", 4);
                startActivity(intent);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ListupActivity.class);
                intent.putExtra("tabIdx", 5);
                startActivity(intent);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ListupActivity.class);
                intent.putExtra("tabIdx", 6);
                startActivity(intent);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                MyAlertDialogFragment newDialogFragment
                        = MyAlertDialogFragment.newInstance("관심 카테고리 편집");
                newDialogFragment.show(getFragmentManager(), "dialog");
            }

        });
        return rootView;
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
        //     binding = null;
    }
}