package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.example.moamoa.ui.home.homelist_adapter;
import com.example.moamoa.ui.home.homelist_data;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    public View getView(int pos, View converview, ViewGroup viewGroup) {
        View view = mLayoutInflater.inflate(R.layout.partylist_parts, null);
        ImageView profile_img = (ImageView) view.findViewById(R.id.party_profileimage);
        TextView nickname   = (TextView) view.findViewById(R.id.party_nickname);
        TextView lastchat   = (TextView) view.findViewById(R.id.party_lastchat);
        TextView partdate   = (TextView) view.findViewById(R.id.party_date);

        nickname.setText(list.get(pos).getNickname());
        lastchat.setText(list.get(pos).getLastchat());
        partdate.setText(list.get(pos).getParti_date());
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference pathReference = firebaseStorage.getReference(list.get(pos).getProfile());
        Activity context2 = (Activity) profile_img.getContext();
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (context2.isFinishing()) return;
                Glide.with(profile_img.getContext())
                        .load(uri)
                        .into(profile_img);
            }
        });
        return view;
    }

}
