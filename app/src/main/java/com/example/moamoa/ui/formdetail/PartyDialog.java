package com.example.moamoa.ui.formdetail;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.moamoa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PartyDialog extends DialogFragment {
    private Fragment fragment;
    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    private View view;
    private ArrayList<PartyListData> partylist=new ArrayList();
    String FID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view            = inflater.inflate(R.layout.partylist,container,false);
        firebaseStorage = FirebaseStorage.getInstance();
        mDatabase       = FirebaseDatabase.getInstance().getReference();

        Bundle mArgs = getArguments();
        FID = mArgs.getString("FID");
        if (fragment != null) {
            DialogFragment dialogFragment = (DialogFragment) fragment;
            dialogFragment.dismiss();
        }


        InitializePartiform();

        return view;
    }

    public void printPartyNumber(){
    }
    public void InitializePartiform()
    {
        ArrayList<PartyListData> partylist=new ArrayList();
        PartyListData listdata = new PartyListData();
        TextView partynumber = (TextView) view.findViewById(R.id.partylist_number);
        mDatabase.child("party").child(FID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                partynumber.setText(task.getResult().getChildrenCount()+"");
                for(DataSnapshot dataSnapshot : task.getResult().getChildren()){
                    String uid = dataSnapshot.getKey()+"";
                    String date = dataSnapshot.getValue()+"";
                    mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            //listdata.set(task.getResult().child())
                            Log.e("party_task",task.getResult()+"");
                            listdata.setUID(uid);
                            listdata.setNickname(task.getResult().child("nick").getValue()+"");
                            listdata.setParti_date(date);
                            listdata.setProfile(task.getResult().child("image").getValue()+"");
                            listdata.setLastchat("마지막 채팅이면 좋겠는데 빡세네");
                            partylist.add(listdata);
                        }
                    });
                }
            }
        });
    }
    public void FragmentDialog() {
    }
}
