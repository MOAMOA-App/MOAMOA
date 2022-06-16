package com.example.moamoa.ui.chats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.Holder> {
    private Context context;
    private List<ChatsData> list = new ArrayList<>();
    private String leftname;

    public ChatsAdapter(Context context, List<ChatsData> list, String leftname){
        this.context = context;
        this.list = list;
        this.leftname = leftname;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_chats, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 각 위치에 문자열 세팅

        ChatsData chats = list.get(position);

        holder.nickName.setText(chats.nickname);
        holder.Message.setText(chats.chatmsg);
        holder.sendedTime.setText(chats.sendedtime);

        if (list.get(position).getLeftname().equals(this.leftname)){
            // 내가 보낸 메시지일시 오른쪽에서 출력:왼쪽 이미지
            holder.nickName.setVisibility(View.INVISIBLE);
            holder.Message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.profile_image.setVisibility(View.INVISIBLE); //프사 안보이게

            holder.chatLayout.setGravity(Gravity.END);
            holder.LinearChatMsg.setGravity(Gravity.END);
        }
        else{
            // 남이 보낸 메시지일시 왼쪽에서 출력
            holder.nickName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.nickName.setGravity(Gravity.START);
            holder.Message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            holder.chatLayout.setGravity(Gravity.START);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        //return list.size();  //RecyclerView의 size return
        //삼항 연산자
        return list == null ? 0 :  list.size();
        //return comments.size();
    }

    public ChatsData getChat(int position) {
        return list != null ? list.get(position) : null;
    }

    public void addChat(ChatsData chat) {
        list.add(chat);
        notifyItemInserted(list.size()-1); //갱신
        // EX: 0, 1, 2 = 3번지. ~>오류날수도.
        // 사이즈가 3개면 3번지에 넣으란 뜻인데 저기에는 3번지 없음--> 2에 넣는다(list.size()-1)
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView nickName, Message, sendedTime;
        ImageView profile_image;
        CardView cv;

        LinearLayout chatLayout, LinearChatMsg;

        public View rootView;

        public Holder(View view){
            super(view);
            profile_image = (ImageView) view.findViewById(R.id.profile_image);
            nickName = (TextView) view.findViewById(R.id.chat_nickname);
            Message = (TextView) view.findViewById(R.id.chat_msg);
            cv = (CardView) view.findViewById(R.id.chat_cardview);
            sendedTime = (TextView) view.findViewById(R.id.sended_time);

            chatLayout = (LinearLayout) view.findViewById(R.id.chatting_layout);
            LinearChatMsg = (LinearLayout) view.findViewById(R.id.Linear_chatmsg);

            rootView = view;
        }
    }
}
