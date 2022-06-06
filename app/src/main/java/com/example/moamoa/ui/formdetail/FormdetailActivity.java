package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.moamoa.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FormdetailActivity extends Activity {
    private DatabaseReference mDatabase;
    private HashMap<String, String> list;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formdetail);

        Intent intent = getIntent();
        String temp = intent.getStringExtra("FID");
        list = new HashMap<>();
        list.put("FID",intent.getStringExtra("FID"));
        list.put("Subject",intent.getStringExtra("subject"));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Forms");
        mDatabase.child(temp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("", String.valueOf(dataSnapshot));
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        return;
    }

    private void Initializeform(HashMap map) {
        TextView nametv = (TextView) findViewById(R.id.name);
        TextView subjtv = (TextView) findViewById(R.id.title);
        //nametv.setText(FID);
        subjtv.setText(map.get("Subject").toString());
    }

}
