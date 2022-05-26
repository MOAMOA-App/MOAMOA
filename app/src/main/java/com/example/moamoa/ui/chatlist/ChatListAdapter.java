package com.example.moamoa.ui.chatlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;
import com.example.moamoa.ui.chats.ChatsActivity;

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

    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView formName;
        public TextView recentMessage;


        public Holder(View view){
            super(view);
            formName = (TextView) view.findViewById(R.id.chat_formname);
            recentMessage = (TextView) view.findViewById(R.id.chat_recent);

            // recyclerview 클릭 이벤트 - ChatsActivity와 연결
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, ChatsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
