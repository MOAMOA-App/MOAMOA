package com.example.moamoa.ui.formdetail;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
        Button close_btn = (Button) view.findViewById(R.id.partylist_closebtn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartyDialog.this.dismiss();
            }
        });
        return view;
    }

    public void InitializePartiform()
    {

        TextView partynumber = (TextView) view.findViewById(R.id.partylist_number);
        ListView listView = (ListView)view.findViewById(R.id.partylist_view) ;
        mDatabase.child("party").child(FID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int count = (int) task.getResult().getChildrenCount();
                final int[] x = {0};
                partynumber.setText(count+"");
                for(DataSnapshot dataSnapshot : task.getResult().getChildren()){
                    String uid = dataSnapshot.getKey()+"";
                    String date = dataSnapshot.getValue()+"";
                    mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            PartyListData listdata = new PartyListData();
                            listdata.setUID(uid);
                            listdata.setNickname(task.getResult().child("nick").getValue()+"");
                            listdata.setParti_date(date.substring(4,6)+"/"+date.substring(6,8)+" "+date.substring(8,10)+":"+date.substring(10,12));
                            listdata.setProfile(task.getResult().child("image").getValue()+"");
                            listdata.setLastchat("");
                            partylist.add(listdata);
                            x[0]++;
                            if(x[0] ==count){
                                partynumber.setText(count+"");
                                PartyListAdapter myAdapter = new PartyListAdapter(getContext(),partylist);
                                listView.setAdapter(myAdapter);
                            }
                        }
                    });

                }

            }
        });
    }
}
