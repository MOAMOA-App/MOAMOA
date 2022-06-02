package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.moamoa.R;
import com.example.moamoa.databinding.ActivityFormdetailBinding;

public class show_detail extends Activity {
    private ActivityFormdetailBinding binding;
    private TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityFormdetailBinding.inflate(LayoutInflater.from(getBaseContext()));
        //setContentView(binding.getRoot());
        Intent intent = getIntent();
        String text = intent.getStringExtra("name");
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(text);
        setContentView(R.layout.fragment_formdetail);
    }

}
