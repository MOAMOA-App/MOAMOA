package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Context;
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

public class NoticeAdapter extends BaseAdapter {
    private ArrayList<NoticeData> list = new ArrayList<>();
    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;

    public NoticeAdapter(Context context, ArrayList<NoticeData> data) {
        mContext = context;
        list = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) { return list.get(pos); }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View converview, ViewGroup viewGroup) {
        View view = mLayoutInflater.inflate(R.layout.create_noticepart, null);
        TextView subject   = (TextView) view.findViewById(R.id.notice_part_subject);
        TextView text   = (TextView) view.findViewById(R.id.notice_part_text);
        TextView date   = (TextView) view.findViewById(R.id.notice_part_date);

        subject.setText(list.get(pos).getSubject());
        text.setText(list.get(pos).getText());
        date.setText(list.get(pos).getDate());
        /*
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

         */
        return view;
    }
}
