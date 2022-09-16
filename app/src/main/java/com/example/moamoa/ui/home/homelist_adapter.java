package com.example.moamoa.ui.home;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class homelist_adapter extends RecyclerView.Adapter<homelist_adapter.ViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<homelist_data> arrayList;
    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_main;
        TextView txt_title;
        TextView txt_name;
        TextView txt_mans;
        TextView txt_FID;
        TextView txt_UID;
        TextView txt_cateog;
        TextView txt_state;

        public ViewHolder(View view) {
            super(view);
            Log.i("view",view+"");
            // Define click listener for the ViewHolder's View
            img_main    = (ImageView)view.findViewById(R.id.homelist_mainImage);
            txt_title   = (TextView) view.findViewById(R.id.homelist_title);
            txt_name    = (TextView) view.findViewById(R.id.homelist_name);
            txt_mans    = (TextView) view.findViewById(R.id.homelist_mans);
            txt_FID     = (TextView) view.findViewById(R.id.homelist_FID);
            txt_UID     = (TextView) view.findViewById(R.id.homelist_UID);
            txt_cateog  = (TextView) view.findViewById(R.id.homelist_category);
            txt_state   = (TextView) view.findViewById(R.id.homelist_state);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, pos) ;
                        }
                    }
                }
            });
        }

    }

    public homelist_adapter(ArrayList<homelist_data> arrayList) {
        this.arrayList = arrayList;
    }
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homelistview, parent,false);
        return new ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        homelist_data item = arrayList.get(position);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference pathReference = firebaseStorage.getReference(item.getImgName());
        Activity context = (Activity) viewHolder.img_main.getContext();
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (context.isFinishing()) return;
                Glide.with(viewHolder.img_main.getContext())
                        .load(uri)
                        .into(viewHolder.img_main);
            }
        });

        //viewHolder.img_main.setImageResource();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.child(item.getNick()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewHolder.txt_name.setText((dataSnapshot.child("nick").getValue().toString()));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }

        });
        viewHolder.txt_cateog.setText(item.getCategory());
        viewHolder.txt_title.setText(item.getTitle());
        viewHolder.txt_UID.setText(item.getUID());
        viewHolder.txt_mans.setText(item.getMans());
        viewHolder.txt_FID.setText(item.getFID());

        switch(item.getState()){
            case "0":
                viewHolder.txt_state.setText("참여모집");
                viewHolder.txt_state.setTextColor(Color.parseColor("#F1A94E"));
                viewHolder.txt_mans.setTextColor(Color.parseColor("#F1A94E"));
                break;
            case "1":
                viewHolder.txt_state.setText("거래진행");
                viewHolder.txt_state.setTextColor(Color.parseColor("#274E13"));
                viewHolder.txt_mans.setTextColor(Color.parseColor("#274E13"));
                break;
            case "2":
                viewHolder.txt_state.setText("거래완료");
                viewHolder.txt_state.setTextColor(Color.parseColor("#4C4C4C"));
                viewHolder.txt_mans.setTextColor(Color.parseColor("#4C4C4C"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getState());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
