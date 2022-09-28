package com.example.moamoa.ui.home;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
        TextView txt_dead;
        TextView txt_mans;
        TextView txt_FID;
        TextView txt_UID;
        TextView txt_cateog;
        TextView txt_location;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            img_main    = (ImageView)view.findViewById(R.id.homelist_mainImage);
            txt_title   = (TextView) view.findViewById(R.id.homelist_title);
            txt_dead    = (TextView) view.findViewById(R.id.homelist_deadline);
            txt_mans    = (TextView) view.findViewById(R.id.homelist_mans);
            txt_FID     = (TextView) view.findViewById(R.id.homelist_FID);
            txt_UID     = (TextView) view.findViewById(R.id.homelist_UID);
            txt_cateog  = (TextView) view.findViewById(R.id.homelist_category);
            txt_location= (TextView) view.findViewById(R.id.homelist_location);

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
    @RequiresApi(api = Build.VERSION_CODES.M)
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

        viewHolder.txt_cateog.setText(item.getCategory());
        viewHolder.txt_title.setText(item.getTitle());
        viewHolder.txt_UID.setText(item.getUID());
        viewHolder.txt_mans.setText(item.getMans());
        viewHolder.txt_FID.setText(item.getFID());
        viewHolder.txt_location.setText(item.getLocation());
        switch(item.getDead()){
            case 0:
                viewHolder.txt_dead.setText("마감임박");
                viewHolder.txt_dead.setTextAppearance(R.style.DEADLINE_boldText);
                break;
            case 1:
            case 2:
            case 3:
                viewHolder.txt_dead.setText(item.getDead()+"일 뒤 마감");
                break;
            default:
                if(item.getDead()<0){
                    viewHolder.txt_dead.setText("기한 종료");
                    viewHolder.txt_dead.setTextColor(Color.parseColor("#4C4C4C"));
                }else{
                    viewHolder.txt_dead.setText(item.getDead()+"일 뒤 마감");
                    viewHolder.txt_dead.setTextColor(Color.parseColor("#000000"));
                }
                break;
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
