package com.example.moamoa.ui.chats;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.Holder> {
    private Context context;
    private ArrayList<ChatsUserData> arrayList = null;

    public ChatUserAdapter(Context context, ArrayList<ChatsUserData> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_chats_userinfo, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        // 프사 연결
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference pathReference = firebaseStorage.getReference(arrayList.get(position).profilepic);

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ChatsActivity context = (ChatsActivity) holder.ProfileImage.getContext();
                if (context.isFinishing()) return;
                Glide.with(holder.ProfileImage.getContext())
                        .load(uri)
                        .into(holder.ProfileImage);
            }
        });

        // 닉네임 연결
        holder.UserNickname.setText(arrayList.get(position).usernick);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        public ImageView ProfileImage;
        public TextView UserNationality;
        public TextView UserNickname;
        public TextView AccountOwner;

        public Holder(View view){
            super(view);

            ProfileImage =(ImageView)view.findViewById(R.id.chats_profile_image);
            UserNationality = (TextView) view.findViewById(R.id.chats_nationality);
            UserNickname = (TextView) view.findViewById(R.id.chats_TextView_nickname);
            AccountOwner = (TextView) view.findViewById(R.id.accountowner);
        }
    }
}
