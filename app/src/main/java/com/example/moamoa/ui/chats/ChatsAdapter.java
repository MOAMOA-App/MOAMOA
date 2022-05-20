package com.example.moamoa.ui.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;

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
        holder.leftNickName.setText(list.get(itemposition).leftname);
        holder.leftMessage.setText(list.get(itemposition).leftmessage);
        holder.rightNickName.setText(list.get(itemposition).rightname);
        holder.rightMessage.setText(list.get(itemposition).rightmessage);
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return list.size();
    }   // RecyclerView의 size return

    public class Holder extends RecyclerView.ViewHolder{
        public TextView leftNickName;
        public TextView leftMessage;
        public TextView rightNickName;
        public TextView rightMessage;

        public Holder(View view){
            super(view);
            leftNickName = (TextView) view.findViewById(R.id.TextView_nickname_left);
            leftMessage = (TextView) view.findViewById(R.id.TextView_message_left);
            rightNickName = (TextView) view.findViewById(R.id.TextView_nickname_right);
            rightMessage = (TextView) view.findViewById(R.id.TextView_message_right);
        }
    }
}
