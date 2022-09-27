package com.example.moamoa.ui.chats;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.Form;

import java.util.ArrayList;

public class ChatsFormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Form> arrayList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
