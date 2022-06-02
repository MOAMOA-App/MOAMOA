package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.moamoa.R;
import com.google.firebase.database.DatabaseReference;

public class FormdetailActivity extends Activity {
    private DatabaseReference mDatabase;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formdetail);

        Intent intent = getIntent();
        String FID = intent.getStringExtra("FID");
        String subjecttext;
        /*
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Forms");
        mDatabase.child(FID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    String subjecttext = (String) fileSnapshot.getValue();
                    Log.w("", subjecttext);
                    Initializeform();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        */

        return;
    }

    private void Initializeform() {
        TextView nametv = (TextView) findViewById(R.id.name);
        TextView subjtv = (TextView) findViewById(R.id.title);
        //nametv.setText(FID);
        //subjtv.setText(subjecttext);
    }

}
