package com.example.moamoa.ui.formdetail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.ui.home.homelist_adapter;
import com.example.moamoa.ui.home.homelist_data;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PartyListAdapter extends BaseAdapter {
    private ArrayList<PartyListData> list = new ArrayList<>();
    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;

    public PartyListAdapter(Context context, ArrayList<PartyListData> data) {
        mContext = context;
        list = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return list.size();
    }
    //https://lktprogrammer.tistory.com/163
    @Override
    public Object getItem(int pos) { return list.get(pos); }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        CircleImageView profile_img = (CircleImageView) view.findViewById(R.id.party_profileimage);
        TextView nickname   = (TextView) view.findViewById(R.id.party_nickname);
        TextView lastchat   = (TextView) view.findViewById(R.id.party_lastchat);
        TextView partdate   = (TextView) view.findViewById(R.id.party_date);

        nickname.setText(list.get(pos).getNickname());
        lastchat.setText(list.get(pos).getLastchat());
        partdate.setText(list.get(pos).getParti_date());
        return view;
    }
    /*
    private ArrayList<PartyListData> list = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private homelist_adapter.OnItemClickListener mListener = null;
        public CircleImageView profile_img;
        public TextView nickname, lastchat, parti_date;
        public ViewHolder(View view) {
            super(view);
            profile_img = (CircleImageView) view.findViewById(R.id.party_profileimage);
            nickname = (TextView) view.findViewById(R.id.party_nickname);
            parti_date = (TextView) view.findViewById(R.id.party_date);
            lastchat = (TextView) view.findViewById(R.id.party_lastchat);


        }
    }
    public PartyListAdapter(ArrayList<PartyListData> arrayList) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partylist_parts, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PartyListData item = list.get(position);
        Log.e("adf",item.getNickname());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    */
}
