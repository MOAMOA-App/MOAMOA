package com.example.moamoa.ui.formdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentFormdetailBinding;

public class FormdetailFragement extends Fragment {
    private FragmentFormdetailBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentFormdetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

}
