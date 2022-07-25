package com.example.moamoa.ui.category;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

            TextView tv_num = (TextView) convertView.findViewById(R.id.cate_numb);
            TextView tv_name = (TextView) convertView.findViewById(R.id.cate_name);
            ImageView tv_img = convertView.findViewById(R.id.cate_image);

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference pathReference = firebaseStorage.getReference(bearItem.getName()+".png");


            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(tv_img.getContext())
                            .load(uri)
                            .into(tv_img);
                }
            });
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
