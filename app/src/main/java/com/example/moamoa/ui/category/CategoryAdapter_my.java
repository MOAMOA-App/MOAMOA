package com.example.moamoa.ui.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.moamoa.R;

import java.util.ArrayList;

public class CategoryAdapter_my extends BaseAdapter {
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
            convertView = inflater.inflate(R.layout.category_mybtn_design, viewGroup, false);

            TextView tv_num = (TextView) convertView.findViewById(R.id.cate_numb);
            TextView tv_name = (TextView) convertView.findViewById(R.id.cate_name);

            tv_num.setText(bearItem.getNumb());
            tv_name.setText(bearItem.getName());

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        return convertView;  //뷰 객체 반환
    }

    public void clear() {
        items.clear();
        items.isEmpty();
    }
}
