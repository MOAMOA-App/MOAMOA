package com.example.moamoa;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.moamoa.R;

public class SingerItemView extends LinearLayout {

    //어디서든 사용할 수 있게하려면
    TextView nameView, addressView, priceView, titleView ;
    ImageView imageView;

    public SingerItemView(Context context) {
        super(context);
        init(context);//인플레이션해서 붙여주는 역
    }

    public SingerItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // 지금 만든 객체(xml 레이아웃)를 인플레이션화(메모리 객체화)해서 붙여줌
    // LayoutInflater를 써서 시스템 서비스를 참조할 수 있음
    // 단말이 켜졌을 때 기본적으로 백그라운드에서 실행시키는 것을 시스템 서비스라고 함
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview,this, true);

        nameView = findViewById(R.id.nameView);
        addressView = findViewById(R.id.addressView);
        titleView = findViewById(R.id.titleView);
        priceView = findViewById(R.id.priceView);
        imageView = findViewById(R.id.imageView);
    }

    public void setName(String name){
        nameView.setText(name);
    }
    public void setAddress(String address){
        addressView.setText(address);
    }
    public void setTitle(String title){
        titleView.setText(title);
    }
    public void setPrice(String price){
        priceView.setText(price);
    }

    public void setImage(int resId){
        imageView.setImageResource(resId);
    }
}