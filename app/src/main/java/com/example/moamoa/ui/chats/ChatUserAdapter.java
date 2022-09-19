package com.example.moamoa.ui.chats;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moamoa.R;
import com.example.moamoa.ui.home.homelist_data;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {
    private ArrayList<ChatsUserData> arrayList = null;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ProfileImage;
        public TextView UserNationality;
        public TextView UserNickname;
        public TextView UID;

        public ViewHolder(View view){
            super(view);
            ProfileImage =(ImageView)view.findViewById(R.id.chats_profile_image);
            UserNationality = (TextView) view.findViewById(R.id.chats_nationality);
            UserNickname = (TextView) view.findViewById(R.id.chats_TextView_nickname);
            UID = (TextView) view.findViewById(R.id.chats_recyclerview_userinfo_UID);
        }
    }

    public ChatUserAdapter(ArrayList<ChatsUserData> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_chats_userinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ChatsUserData item = arrayList.get(position);

        // 프사 불러옴
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference pathReference = firebaseStorage.getReference(item.getProfilepic());
        Activity context = (Activity) viewHolder.ProfileImage.getContext();
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (context.isFinishing()) return;
                Glide.with(viewHolder.ProfileImage.getContext())
                        .load(uri)
                        .into(viewHolder.ProfileImage);
            }
        });

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        // 닉네임 설정
        mDatabase.child(item.getUsernick()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewHolder.UserNickname.setText((dataSnapshot.child("nick").getValue().toString()));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }

        });

        viewHolder.UserNationality.setText(item.getNationality());
        viewHolder.UID.setText(item.getUID());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
