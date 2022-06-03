package com.example.moamoa.ui.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.R;

import java.util.ArrayList;
import java.util.List;

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
        Holder holder = new Holder(view);
        return holder;
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
            holder.nickName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.Message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.sendedTime.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.profile_image.setVisibility(View.INVISIBLE); //프사 안보이게
        }
        else{
            // 남이 보낸 메시지일시 왼쪽에서 출력
            holder.nickName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.Message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.sendedTime.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        //return list.size();  //RecyclerView의 size return
        //삼항 연산자
        return list == null ? 0 :  list.size();
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
        public View rootView;
        //CardView cv;

        public Holder(View view){
            super(view);
            nickName = (TextView) view.findViewById(R.id.chat_nickname);
            Message = (TextView) view.findViewById(R.id.chat_msg);
            sendedTime = (TextView) view.findViewById(R.id.sended_time);
            //cv = (CardView) view.findViewById(R.id.chat_cardview);

            profile_image = (ImageView) view.findViewById(R.id.profile_image);

            rootView = view;


        }
    }
}
