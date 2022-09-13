package com.example.moamoa.ui.category;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/*
* 데이터를 listview안에 넣는
* 클릭하면 상세폼으로 이동 찜버튼 누르기
* */
public class CustomListView extends BaseAdapter {
    LayoutInflater layoutInflater = null;
    private ArrayList<Form> listViewData = null;
    private int count = 0;
    String k="";
    String v="";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public CustomListView(ArrayList<Form> listData) {
        listViewData = listData;
        count = listViewData.size();
    }
    FirebaseDatabase firebaseDatabase;
    int num_a;
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final Context context = parent.getContext();
            if (layoutInflater == null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = layoutInflater.inflate(R.layout.fragment_main, parent, false);
        }
        ImageView mainImage = convertView.findViewById(R.id.mainImage);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference pathReference = firebaseStorage.getReference(listViewData.get(position).image);


        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Activity context = (Activity) mainImage.getContext();
                if(context.isFinishing()) return;
                Glide.with(mainImage.getContext())
                        .load(uri)
                        .into(mainImage);
            }
        });

        TextView title = convertView.findViewById(R.id.title);
        TextView name = convertView.findViewById(R.id.name);
        TextView charge = convertView.findViewById(R.id.charge);
        TextView mans = convertView.findViewById(R.id.mans);

        //mainImage.listViewData.get(position).photo);
        title.setText(listViewData.get(position).subject);
        name.setText(listViewData.get(position).address);
        charge.setText(listViewData.get(position).cost+"원");
        mans.setText(listViewData.get(position).parti_num+"/"+listViewData.get(position).max_count);
        //listview와 버튼 클릭 다르게 주기

        ToggleButton button = convertView.findViewById(R.id.heart);
        FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    k=dataSnapshot.getKey();
                    if (dataSnapshot.getValue().equals("true")) {
                        button.setBackgroundResource(R.drawable.full_heart);
                    } else {
                        button.setBackgroundResource(R.drawable.empty_heart);
                    }
                }
                    Log.d("MainActivity", "ValueEventListener : " + dataSnapshot.getKey());
                    Log.d("MainActivity", "ValueEventListener : " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {    }
        });

        //FirebaseDatabase.getInstance().getReference("form").child(listViewData.get(position).FID).child("heart_num").setValue(num_a+1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        v= String.valueOf(dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {    }
                });


                Log.d("MainActivity", "vv: " + v);

                if (button.isChecked()){
                    //button.setBackgroundResource(R.drawable.full_heart);
                    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).setValue("true");
                }
                else if( !button.isChecked())
                {
                   // button.setBackgroundResource(R.drawable.empty_heart);
                    //FirebaseDatabase.getInstance().getReference("form").child(listViewData.get(position).FID).child("heart_num").setValue(num_a-1);
                    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).setValue("false");
                }
                //{
                //    button.setBackgroundResource(R.drawable.full_heart);
                //    FirebaseDatabase.getInstance().getReference("form").child(listViewData.get(position).FID).child("heart_num").setValue(num_a+1);
                //    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).setValue("true");

                //}
                //if (isChecked ){





                }

        });

        return convertView;
    } // getView



}


