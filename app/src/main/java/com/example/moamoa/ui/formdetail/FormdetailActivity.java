package com.example.moamoa.ui.formdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moamoa.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FormdetailActivity extends Activity {
    private DatabaseReference mDatabase;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formdetail);

        Intent intent = getIntent();
        String temp = intent.getStringExtra("FID");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("form");
        mDatabase.child(temp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String subject = dataSnapshot.child("subject").getValue().toString();
                //String category = dataSnapshot.child("category").getValue().toString();
                String text = dataSnapshot.child("text").getValue().toString();
                String cost = dataSnapshot.child("cost").getValue().toString();
                String max_count = dataSnapshot.child("max_count").getValue().toString();

                String UID = dataSnapshot.child("UID_dash").getValue().toString();
                UserFind(UID);
                Initializeform(subject,"category",text,cost,max_count);
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
    private void UserFind(String UID){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView name_text = (TextView) findViewById(R.id.detail_nick);
                ImageView profil_text = (ImageView) findViewById(R.id.detail_profile);
                name_text.setText(dataSnapshot.child("image").getValue().toString());
                /*
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                mDatabase.child(item.getNick()).child("nick").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.txt_name.setText((dataSnapshot.getValue().toString()));

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }

                });

                 */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
    private void Initializeform(String subject,String category,String text,String cost,String max_count) {
        TextView subject_text = (TextView) findViewById(R.id.detail_subject);
        TextView category_text = (TextView) findViewById(R.id.detail_category);
        TextView text_text = (TextView) findViewById(R.id.detail_textarea);
        TextView cost_text = (TextView) findViewById(R.id.detail_cost);
        TextView max_count_text = (TextView) findViewById(R.id.detail_category);
        subject_text.setText(subject);
        category_text.setText("1");
        text_text.setText(text);
        cost_text.setText(cost);
    }

}
