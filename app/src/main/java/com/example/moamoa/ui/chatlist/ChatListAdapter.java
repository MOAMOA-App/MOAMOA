package com.example.moamoa.ui.chatlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {
    private Context context;
    private List<ChatListData> list = new ArrayList<>();

    public ChatListAdapter(Context context, List<ChatListData> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chatlist, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 각 위치에 문자열 세팅
        int itemposition = position;
        holder.formName.setText(list.get(itemposition).formname);
        holder.recentMessage.setText(list.get(itemposition).recentmessage);
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return list.size();
    }   // RecyclerView의 size return

    public class Holder extends RecyclerView.ViewHolder{
        public TextView formName;
        public TextView recentMessage;
        public TextView item_Eliment;

        public Holder(View view){
            super(view);
            formName = (TextView) view.findViewById(R.id.chat_formname);
            recentMessage = (TextView) view.findViewById(R.id.chat_recent);

            item_Eliment = itemView.findViewById(R.id.chats_recyclerview);
        }
    }
}
