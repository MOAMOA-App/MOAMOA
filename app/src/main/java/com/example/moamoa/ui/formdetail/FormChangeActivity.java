package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.moamoa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

public class FormChangeActivity extends Activity {
    private DatabaseReference mDatabase;
    private FirebaseStorage   firebaseStorage;
    private String FID;
    private String FUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formchange);

        Intent intent = getIntent();
        FID = intent.getStringExtra("FID");
        FUID = intent.getStringExtra("UID_dash");

        firebaseStorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        TextView formname = (TextView)findViewById(R.id.change_subject);
        TextView cost = (TextView)findViewById(R.id.change_cost);
        TextView text = (TextView)findViewById(R.id.change_text);
        TextView max_people = (TextView)findViewById(R.id.change_max_people);
        TextView address = (TextView)findViewById(R.id.change_addr_search);
        TextView address_alpha = (TextView)findViewById(R.id.change_addr_detail);
        mDatabase.child("form").child(FID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.e("output",task.getResult().toString());
                formname.setText(task.getResult().child("subject").getValue().toString());
                cost.setText(task.getResult().child("cost").getValue().toString());
                text.setText(task.getResult().child("text").getValue().toString());
                max_people.setText(task.getResult().child("max_count").getValue().toString());
                address.setText(task.getResult().child("address").getValue().toString());
                address_alpha.setText(task.getResult().child("addr_detail").getValue().toString());
            }
        });
    }
}
