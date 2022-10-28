package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NoticeAdapter extends BaseAdapter {
    private ArrayList<NoticeData> list = new ArrayList<>();
    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private DatabaseReference mDatabase;
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View view = mLayoutInflater.inflate(R.layout.create_noticepart, null);
        TextView subject    = (TextView) view.findViewById(R.id.notice_part_subject);
        TextView text       = (TextView) view.findViewById(R.id.notice_part_text);
        TextView date       = (TextView) view.findViewById(R.id.notice_part_date);

        subject.setText(list.get(pos).getSubject());
        text.setText(list.get(pos).getText());


        String temp = list.get(pos).getDate();
        temp = temp.substring(2,4)+"/"+temp.substring(4,6)+"/"+temp.substring(6,8)+" "+temp.substring(8,10)+":"+temp.substring(10,12);
        date.setText(temp);

        int s = list.get(pos).getSide();
        if(s==0) {
            String fid = list.get(pos).getFid();
            int numb = list.get(pos).getNumb();
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder delete_alert = new AlertDialog.Builder(viewGroup.getContext());
                    delete_alert.setTitle("공지를 삭제하시겠습니까?")
                        .setMessage("'"+list.get(pos).getSubject()+"'")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabase.child("form").child(fid).child("notice").child(String.valueOf(numb)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(view.getContext(),"공지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
                    return false;
                }
            });
        }
        return view;
    }
}
