package com.example.moamoa.ui.category;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

/*
* 데이터를 listview안에 넣는
* 클릭하면 상세폼으로 이동 찜버튼 누르기
* */
public class CustomListView extends BaseAdapter {
    LayoutInflater layoutInflater = null;
    private ArrayList<Form> listViewData = null;
    private int count = 0;
    String FID;
    int heart_num;
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

    @SuppressLint("SetTextI18n")
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
                //CategoryActivity context = (CategoryActivity) mainImage.getContext();
                //if (context.isFinishing()) return;
                Activity context = (Activity) mainImage.getContext();
                if(context.isFinishing()) return;
                Glide.with(mainImage.getContext())
                        .load(uri)
                        .into(mainImage);
            }
        });

        TextView title  = convertView.findViewById(R.id.title);
        TextView name   = convertView.findViewById(R.id.name);
        TextView charge = convertView.findViewById(R.id.charge);
        TextView mans   = convertView.findViewById(R.id.mans);
        TextView state  = convertView.findViewById(R.id.list_state);

        String state_temp="";
        switch(listViewData.get(position).state){
            case 0:
                state_temp = "참여모집";
                state.setTextColor(Color.parseColor("#F1A94E"));
                break;
            case 1:
                state_temp = "거래진행";
                state.setTextColor(Color.parseColor("#274E13"));
                break;
            case 2:
                state_temp = "거래종료";
                state.setTextColor(Color.parseColor("#4C4C4C"));
                break;
        }
        //mainImage.listViewData.get(position).photo);
        title.setText(listViewData.get(position).subject);
        name.setText(listViewData.get(position).address);
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        charge.setText(myFormatter.format(listViewData.get(position).cost)+"원");
        state.setText("["+state_temp+"]");
        mans.setText(listViewData.get(position).parti_num+"/"+listViewData.get(position).max_count);

        // String x = listViewData.get(position).deadline.substring(0,4)+"/"+listViewData.get(position).deadline.substring(4,6)+"/"+listViewData.get(position).deadline.substring(6,8);
        //mans.setText(x);

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

                if (button.isChecked()) {

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


