package com.example.moamoa.ui.mypage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moamoa.R;

import java.util.ArrayList;

public class AlarmsetAdapter extends BaseAdapter
{
    LayoutInflater layoutInflater = null;
    private ArrayList<Alarmsetlist> listViewData = new ArrayList<Alarmsetlist>();

    public AlarmsetAdapter() {

    }

    @Override
    public int getCount()
    {
        return listViewData.size() ;
    }

    @Override
    public Object getItem(int position)
    {
        return listViewData.get(position) ;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final int pos = position;

        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (layoutInflater == null)
            {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = layoutInflater.inflate(R.layout.alarmsetting_list, parent, false);
        }


        TextView text = convertView.findViewById(R.id.alarmset_text);

        Alarmsetlist alarmset_list = listViewData.get(position);

        text.setText(alarmset_list.getText());

        return convertView;
    }

    public void addItem(String text) {
        Alarmsetlist item = new Alarmsetlist();

        item.setText(text);

        listViewData.add(item);
    }

}