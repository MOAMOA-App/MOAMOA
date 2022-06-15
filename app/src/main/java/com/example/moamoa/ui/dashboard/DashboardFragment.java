package com.example.moamoa.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moamoa.Form;
import com.example.moamoa.MainActivity;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentDashboardBinding;
import com.example.moamoa.ui.acount.Random_nick;
import com.example.moamoa.ui.acount.User;
import com.example.moamoa.ui.category.CategoryActivity;
import com.example.moamoa.ui.category.CustomListView;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat mFormat1 = new SimpleDateFormat("yyyyMMddhhmmss");
    StorageReference storageRef;
    StorageReference riversRef;
    FirebaseDatabase firebaseDatabase;
    UploadTask uploadTask;
    Uri file;
    String FID;
    int num_a;
    ImageView photo;
    String imageUrl;
    Button button_img;
    private FirebaseStorage storage;
    private final int GALLERY_CODE = 10;
    private FirebaseAuth firebaseAuth;
    int i = 1;

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        return mFormat.format(mDate);
    }
    private String getTime1(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        return mFormat1.format(mDate);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView today = (TextView) root.findViewById(R.id.text_dashboardstart);
        EditText subject = (EditText) root.findViewById(R.id.subject);
        EditText text = (EditText) root.findViewById(R.id.text);
        EditText address = (EditText) root.findViewById(R.id.address);
        EditText cost = (EditText) root.findViewById(R.id.cost);
        EditText max_count = (EditText) root.findViewById(R.id.max_count);
        Spinner category_text = (Spinner) root.findViewById(R.id.spinner);
        Button button = (Button) root.findViewById(R.id.button_dashboard);
        Button button_img = (Button) root.findViewById(R.id.button_img);
        root.findViewById(R.id.button_img).setOnClickListener(onClickListener);
        TextView deadline = (TextView) root.findViewById(R.id.text_dashboardend);
        today.setText(getTime());
        cost.addTextChangedListener(new CustomTextWatcher(cost));
        photo = (ImageView) root.findViewById(R.id.imageView);
        storage = FirebaseStorage.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user1= snapshot.getValue(User.class);
                num_a=user1.getnum()+1;
                Log.d("확인","message : "+num_a);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FID=user.getUid()+num_a;
//if (!(subject.equals("") && text.equals("") &&address.equals("")&&cost.equals("")&&max_count.equals("")&&deadline.equals(""))) {

                Form form = new Form(
                        FID,
                        user.getUid(),
                        "photo/"+FID+".png",
                        subject.getText().toString(),
                        text.getText().toString(),
                        address.getText().toString(),
                        category_text.getSelectedItem().toString(),
                        cost.getText().toString(),
                        max_count.getText().toString(),
                        deadline.getText().toString(),
                        getTime1()
                );


                Log.i("num",num_a+"");
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("num").setValue(num_a);
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child(FID).setValue("host");


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users");


               // childUpdates.put(user.getUid(), postValues);
               // reference.updateChildren(childUpdates);

                storageRef = storage.getReference();
                riversRef = storageRef.child("photo/"+user.getUid() +num_a +".png");

                uploadTask = riversRef.putFile(file);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getContext().getApplicationContext().this,"정상 업로드 안됨",Toast.LENGTH_SHORT);
                    }

                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(DashboardActivity.this,"업로드",Toast.LENGTH_SHORT);
                    }
                });

                FirebaseDatabase.getInstance().getReference("form").child(FID).setValue(form)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "글쓰기 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "글쓰기 실패", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


        //  final TextView textView = binding.textDashboard;
      //  dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button_img:
                        loadAlbum();
                        break;
                }
            }
        };

        private void loadAlbum(){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent,GALLERY_CODE);
        }
        @Override
        public void onActivityResult(int requestCode, final int resultCode, final Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==GALLERY_CODE){
                file = data.getData();

                try{
                    InputStream in = getContext().getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    photo.setImageBitmap(img);
                    getActivity().findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }



}