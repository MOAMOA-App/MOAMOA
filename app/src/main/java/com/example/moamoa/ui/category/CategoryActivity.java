package com.example.moamoa.ui.category;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moamoa.R;

public class CategoryActivity extends AppCompatActivity {

  //  Button button1, button2;
    CategoryFragment frameLayout1;
   // ListFragment frameLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        frameLayout1 = new CategoryFragment();
     //   frameLayout2 = new ListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, frameLayout1).commit();



    }

    // 인덱스를 통해 해당되는 프래그먼트를 띄운다.

}
