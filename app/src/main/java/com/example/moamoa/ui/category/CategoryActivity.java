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
    public void fragmentChange(int index){
        if(index == 1){
            Intent intent=new Intent(getApplicationContext(),ListupActivity.class);
            startActivity(intent);
        }
        else if(index == 2){
            Intent intent=new Intent(getApplicationContext(),ListupActivity.class);
            startActivity(intent);
        }

        else if (index ==3){
            Intent intent=new Intent(getApplicationContext(),ListupActivity.class);
            startActivity(intent);
        }

        else if (index ==4){
            Intent intent=new Intent(getApplicationContext(),ListupActivity.class);
            startActivity(intent);
        }
        else if (index ==5){
            Intent intent=new Intent(getApplicationContext(),ListupActivity.class);
            startActivity(intent);
        }
        else if (index ==6){
            Intent intent=new Intent(getApplicationContext(),ListupActivity.class);
            startActivity(intent);
        }
        else if (index ==7){
            Intent intent=new Intent(getApplicationContext(),ListupActivity.class);
            startActivity(intent);
        }
    }
}
