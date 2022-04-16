package com.example.moamoa.ui.chats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.ui.dashboard.DashboardData;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.Holder> {
    private Context context;
    private List<ChatsData> list = new ArrayList<>();

    public ChatsAdapter(Context context, List<ChatsData> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chats, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 각 위치에 문자열 세팅
        int itemposition = position;
        holder.nickName.setText(list.get(itemposition).nickname);
        holder.Message.setText(list.get(itemposition).message);
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return list.size();
    }   // RecyclerView의 size return

    public class Holder extends RecyclerView.ViewHolder{
        public TextView nickName;
        public TextView Message;

        public Holder(View view){
            super(view);
            nickName = (TextView) view.findViewById(R.id.TextView_nickname);
            Message = (TextView) view.findViewById(R.id.TextView_message);
        }
    }
}
