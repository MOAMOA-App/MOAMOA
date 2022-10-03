package com.example.moamoa.ui.formdetail;

import static java.lang.String.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CreatorNotice extends DialogFragment {
    private Fragment fragment;
    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    private View view;
    private ArrayList<PartyListData> partylist=new ArrayList();
    String FID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view            = inflater.inflate(R.layout.creator_notice,container,false);
        firebaseStorage = FirebaseStorage.getInstance();
        mDatabase       = FirebaseDatabase.getInstance().getReference();

        Bundle mArgs = getArguments();
        FID = mArgs.getString("FID");
        if (fragment != null) {
            DialogFragment dialogFragment = (DialogFragment) fragment;
            dialogFragment.dismiss();
        }

        Button close_btn = (Button) view.findViewById(R.id.createnotice_closebtn);
        Button create_btn = (Button) view.findViewById(R.id.createnotice_createbtn);
        EditText subject_txt = (EditText) view.findViewById(R.id.createnotice_subject);
        EditText text_txt = (EditText) view.findViewById(R.id.createnotice_text);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = valueOf(subject_txt.getText());
                String text = valueOf(text_txt.getText());
                String today = getToday();
                if(subject.length()>0){
                    if(text.length()>0){
                        AlertDialog.Builder AddParty_alert = new AlertDialog.Builder(getContext());
                        AddParty_alert.setTitle("공지를 작성하시겠습니까?")
                        .setMessage("공지는 게시글 상세페이지에 공개되며 모든 참여자에게 알람이 가게됩니다.")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabase.child("form").child(FID).child("notice").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        String count = String.valueOf(task.getResult().getChildrenCount());
                                        count=String.valueOf(task.getResult().getChildrenCount());
                                        mDatabase.child("form").child(FID).child("notice").child(count).child("n_subject").setValue(subject);
                                        mDatabase.child("form").child(FID).child("notice").child(count).child("n_text").setValue(text);
                                        mDatabase.child("form").child(FID).child("notice").child(count).child("n_date").setValue(today);
                                        Toast.makeText(getContext(), "공지를 작성했습니다", Toast.LENGTH_SHORT).show();
                                        CreatorNotice.this.dismiss();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();

                    }else{
                        Toast.makeText(getContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatorNotice.this.dismiss();
            }
        });
        return view;
    }
    private String getToday(){
        SimpleDateFormat mFormat1 = new SimpleDateFormat("yyyyMMddhhmmss");
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);

        return mFormat1.format(mDate);
    }
}
