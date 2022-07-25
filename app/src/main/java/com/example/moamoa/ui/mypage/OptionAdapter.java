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

public class OptionAdapter extends BaseAdapter
{
    LayoutInflater layoutInflater = null;
    private ArrayList<optionlist> listViewData = new ArrayList<optionlist>();

    public OptionAdapter() {

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
            convertView = layoutInflater.inflate(R.layout.mypage_list, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.mypage_image);

        TextView text = convertView.findViewById(R.id.mypage_text);

        optionlist option_list = listViewData.get(position);

        image.setImageDrawable(option_list.getImage());
        text.setText(option_list.getText());

        return convertView;
    }

    public void addItem(Drawable image, String text) {
        optionlist item = new optionlist();

        item.setImage(image);
        item.setText(text);

        listViewData.add(item);
    }

}