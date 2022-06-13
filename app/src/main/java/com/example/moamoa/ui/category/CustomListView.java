package com.example.moamoa.ui.category;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.ui.acount.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomListView extends BaseAdapter {
    LayoutInflater layoutInflater = null;
    private ArrayList<Form> listViewData = null;
    private int count = 0;
    String FID;
    int heart_num;
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

        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    FID = listViewData.get(position).FID;

                    //
                    Log.d("확인", "message : " + FID);
                    FirebaseDatabase.getInstance().getReference("form").child(FID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Form form = snapshot.getValue(Form.class);
                            num_a = form.getheart_num() + 1;

                            Log.d("확인", "if : " + num_a);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }

                    });

                    Log.d("확인", "if : " + num_a);

                }


                else{
                    FID = listViewData.get(position).FID;

                    //FirebaseDatabase.getInstance().getReference("form").child(FID).child("heart_num").setValue(num_a);
                    Log.d("확인", "message : " + FID);
                    FirebaseDatabase.getInstance().getReference("form").child(FID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Form form = snapshot.getValue(Form.class);
                            num_a = form.getheart_num() - 1;
                            Log.d("확인", "else : " + num_a);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }


                    });

                    Log.d("확인", "else : " + num_a);
                }


                }





                //
                //String clickName = listViewData.get(position).subject;
                //Log.d("확인","message : "+clickName);



        });

        return convertView;
    } // getView



}


