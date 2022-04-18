package com.example.moamoa.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.Holder> {
    private Context context;
    private List<NotificationsData> list = new ArrayList<>();

    public NotificationsAdapter(Context context, List<NotificationsData> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_notifications, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 각 위치에 문자열 세팅
        int itemposition = position;
        holder.alarmMessage.setText(list.get(itemposition).alarmmessage);
        holder.formName.setText(list.get(itemposition).formname);
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return list.size();
    }   // RecyclerView의 size return

    public class Holder extends RecyclerView.ViewHolder{
        public TextView alarmMessage;
        public TextView formName;

        public Holder(View view){
            super(view);
            alarmMessage = (TextView) view.findViewById(R.id.alarm_message);
            formName = (TextView) view.findViewById(R.id.chat_formname);
        }
    }
}
