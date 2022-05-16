package com.example.moamoa.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;

import java.util.ArrayList;

public class homelist_adapter extends RecyclerView.Adapter<homelist_adapter.ViewHolder> {

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

        public ViewHolder(View view) {
            super(view);
            Log.i("view",view+"");
            // Define click listener for the ViewHolder's View
            img_main = (ImageView) view.findViewById(R.id.mainImage);
            txt_title = (TextView) view.findViewById(R.id.title);
            txt_name = (TextView) view.findViewById(R.id.name);
            txt_mans = (TextView) view.findViewById(R.id.mans);
            txt_FID = (TextView) view.findViewById(R.id.FID);

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
    ArrayList<homelist_data> arrayList = null;
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

        viewHolder.img_main.setImageResource(R.drawable.ic_main_background);
        viewHolder.txt_title.setText(item.getTitle());
        viewHolder.txt_name.setText(item.getName());
        viewHolder.txt_mans.setText(item.getMans());
        viewHolder.txt_FID.setText(item.getFID());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
