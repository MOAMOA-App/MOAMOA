package com.example.moamoa.ui.category;



import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    /* 그리드뷰 어댑터 */
    ArrayList<CategoryData> items = new ArrayList<CategoryData>();

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(CategoryData item) {
        items.add(item);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final CategoryData bearItem = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ((Context) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_btn_design, viewGroup, false);
            final int[] mid = {R.drawable.c_0,R.drawable.c_1,R.drawable.c_2,R.drawable.c_3,R.drawable.c_4,R.drawable.c_5,R.drawable.c_6,R.drawable.c_7,R.drawable.c_8,R.drawable.c_9,R.drawable.c_10,R.drawable.c_11};


            TextView tv_num = (TextView) convertView.findViewById(R.id.cate_numb);
            TextView tv_name = (TextView) convertView.findViewById(R.id.cate_name);


            // drawable 리소스 객체 가져오기

            // XML 에 있는 ImageView 위젯에 이미지 셋팅
            ImageView tv_img = convertView.findViewById(R.id.cate_image);

            int imageResource = mid[Integer.parseInt(bearItem.getNumb())];
            System.out.println("int"+imageResource);
            Drawable image = convertView.getResources().getDrawable(imageResource);
            tv_img.setImageDrawable(image);
            tv_num.setText(bearItem.getNumb());
            tv_name.setText(bearItem.getName());

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        //각 아이템 선택 event
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final List choices = new ArrayList();
                Intent intent = new Intent(context,CategoryActivity.class);
                intent.putExtra("tabIdx",position);
                if(position==1){
                    Intent intent2 = new Intent(context.getApplicationContext(), PlaceholderFragment.class);
                    intent2.putExtra("category", String.valueOf(choices));
                }
                context.startActivity(intent);
            }
        });

        return convertView;  //뷰 객체 반환
    }

}
