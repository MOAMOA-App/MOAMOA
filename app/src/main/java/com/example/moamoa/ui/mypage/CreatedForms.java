package com.example.moamoa.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moamoa.databinding.CreatedFormsBinding;

public class CreatedForms extends Fragment {

    private CreatedFormsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        MypageViewModel mypageViewModel =
                new ViewModelProvider(this).get(MypageViewModel.class);

        binding = CreatedFormsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard2;
        mypageViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //뒤로가기가 home으로 돌아감
        //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        //transaction.remove(this);
        //getActivity().getSupportFragmentManager().popBackStack();
        //transaction.commit();
        //

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}