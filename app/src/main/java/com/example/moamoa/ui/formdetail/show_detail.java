package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityFormdetailBinding;

public class show_detail extends Activity {
    private ActivityFormdetailBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityFormdetailBinding.inflate(LayoutInflater.from(getBaseContext()));
        //setContentView(binding.getRoot());
        setContentView(R.layout.fragment_formdetail);
    }

}
