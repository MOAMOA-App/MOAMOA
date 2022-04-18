package com.example.moamoa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class formlist_adapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<formlist_data> arrayList;

    public formlist_adapter(Context context, ArrayList<formlist_data> data) {
        mContext = context;
        arrayList = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = mLayoutInflater.inflate(R.layout.listview, null);
        ImageView imageView = (ImageView) view1.findViewById(R.id.mainImage);
        TextView title = (TextView) view1.findViewById(R.id.title);
        TextView name = (TextView) view1.findViewById(R.id.name);
        TextView charge = (TextView) view1.findViewById(R.id.charge);
        TextView deadline = (TextView) view1.findViewById(R.id.deadline);

        //imageView.setImageResource(R.drawable.ic_launcher_background);
        title.setText(arrayList.get(i).getTitle());
        name.setText(arrayList.get(i).getName());
        charge.setText(arrayList.get(i).getCharge());
        deadline.setText(arrayList.get(i).getDeadline());

        return view1;
    }
}
