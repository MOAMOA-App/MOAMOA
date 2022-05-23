package com.example.moamoa.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        return mFormat.format(mDate);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView mTextView = (TextView) root.findViewById(R.id.text_dashboardstart);
        EditText e = (EditText) root.findViewById(R.id.cost);
        mTextView.setText(getTime());
        e.addTextChangedListener(new CustomTextWatcher(e));
      //  final TextView textView = binding.textDashboard;
      //  dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}