package com.example.moamoa.ui.category;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.moamoa.R;

import java.util.ArrayList;

public class CustomListView extends BaseAdapter {
    LayoutInflater layoutInflater = null;
    private ArrayList<ListData> listViewData = null;
    private int count = 0;
    public CustomListView(ArrayList<ListData> listData) {
        listViewData = listData;
        count = listViewData.size();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final Context context = parent.getContext();
            if (layoutInflater == null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = layoutInflater.inflate(R.layout.fragment_main, parent, false);
        }

        ImageView mainImage = convertView.findViewById(R.id.mainImage);

        TextView title = convertView.findViewById(R.id.title);
        TextView name = convertView.findViewById(R.id.name);
        TextView charge = convertView.findViewById(R.id.charge);
        TextView mans = convertView.findViewById(R.id.mans);

        mainImage.setImageResource(listViewData.get(position).mainImage);
        title.setText(listViewData.get(position).title);
        name.setText(listViewData.get(position).name);
        charge.setText(listViewData.get(position).charge);
        mans.setText(listViewData.get(position).mans);

        //listview와 버튼 클릭 다르게 주기
        ToggleButton button = convertView.findViewById(R.id.heart);

        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String clickName = listViewData.get(position).title;
                Log.d("확인","message : "+clickName);
            }
        });

        return convertView;
    }



}


