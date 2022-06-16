package com.example.moamoa.ui.category;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.moamoa.Form;
import com.example.moamoa.Heart;
import com.example.moamoa.R;
import com.example.moamoa.ui.acount.User;
import com.example.moamoa.ui.home.homelist_adapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
     //   StorageReference pathReference = firebaseStorage.getReference(listViewData.get(position).image);


     //   pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
     //       @Override
     //       public void onSuccess(Uri uri) {
     //           Glide.with(mainImage.getContext())
     //                   .load(uri)
     //                   .into(mainImage);
     //       }
     //   });




            TextView title = convertView.findViewById(R.id.title);
            TextView name = convertView.findViewById(R.id.name);
            TextView charge = convertView.findViewById(R.id.charge);
            TextView mans = convertView.findViewById(R.id.mans);

            //mainImage.listViewData.get(position).photo);
            title.setText(listViewData.get(position).subject);
            name.setText(listViewData.get(position).address);

            charge.setText(listViewData.get(position).cost);
            mans.setText(listViewData.get(position).deadline);

        //listview와 버튼 클릭 다르게 주기

        ToggleButton button = convertView.findViewById(R.id.heart);
        FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).addValueEventListener(new ValueEventListener() {
            @Override
            public void  onDataChange(DataSnapshot dataSnapshot) {

                Log.d("MainActivity", "ValueEventListener1 : " + dataSnapshot.getKey());

                //  bb="true";

                Log.d("MainActivity", "ValueEventListener : " + dataSnapshot.getValue());
                //              boolean k= (boolean) dataSnapshot.getValue();
                if (dataSnapshot.getValue() != null) {
                    k=dataSnapshot.getKey();
                    v= String.valueOf(dataSnapshot.getValue());
                    if (listViewData.get(position).FID.equals(dataSnapshot.getKey()) & dataSnapshot.getValue().equals("true")) {

                        button.setBackgroundResource(R.drawable.full_heart);

                    } else if (listViewData.get(position).FID.equals(dataSnapshot.getKey()) & dataSnapshot.getValue().equals("false")) {
                        button.setBackgroundResource(R.drawable.empty_heart);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {    }
        });
        //FirebaseDatabase.getInstance().getReference("form").child(listViewData.get(position).FID).child("heart_num").setValue(num_a+1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("form").child( listViewData.get(position).FID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Form form = snapshot.getValue(Form.class);
                        num_a = form.getheart_num() ;

                        Log.d("확인", "버튼 form fid : " + listViewData.get(position).FID);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }

                });



                {
                    Log.d("확인", "fid : " +button.isChecked());
                    button.setBackgroundResource(R.drawable.empty_heart);
                    FirebaseDatabase.getInstance().getReference("form").child(listViewData.get(position).FID).child("heart_num").setValue(num_a-1);
                    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).setValue("false");
                }

                if (v.equals("false")) {

                    button.setBackgroundResource(R.drawable.full_heart);
                    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).setValue("true");
                }

                //{
                //    button.setBackgroundResource(R.drawable.full_heart);
                //    FirebaseDatabase.getInstance().getReference("form").child(listViewData.get(position).FID).child("heart_num").setValue(num_a+1);
                //    FirebaseDatabase.getInstance().getReference("heart").child(user.getUid()).child(listViewData.get(position).FID).setValue("true");

                //}
                //if (isChecked ){





                }





                //
                //String clickName = listViewData.get(position).subject;
                //Log.d("확인","message : "+clickName);



        });

        return convertView;
    } // getView



}


