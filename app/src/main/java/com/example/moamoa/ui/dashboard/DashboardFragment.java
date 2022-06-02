package com.example.moamoa.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.moamoa.ui.category.CategoryActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat mFormat1 = new SimpleDateFormat("yyyyMMddhhmmss");

    private FirebaseAuth firebaseAuth;
    int i = 1;
    String FID;
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
        firebaseAuth =  FirebaseAuth.getInstance();
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView today = (TextView) root.findViewById(R.id.text_dashboardstart);
        EditText subject = (EditText) root.findViewById(R.id.subject);
        EditText text = (EditText) root.findViewById(R.id.text);
        EditText address = (EditText) root.findViewById(R.id.address);
        EditText cost = (EditText) root.findViewById(R.id.cost);
        EditText max_count = (EditText) root.findViewById(R.id.max_count);
        Spinner category_text= (Spinner)root.findViewById(R.id.spinner);
        Button button = (Button) root.findViewById(R.id.button_dashboard);
        TextView deadline = (TextView) root.findViewById(R.id.text_dashboardend);
        today.setText(getTime());
        cost.addTextChangedListener(new CustomTextWatcher(cost));


        //  final TextView textView = binding.textDashboard;
      //  dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//if (!(subject.equals("") && text.equals("") &&address.equals("")&&cost.equals("")&&max_count.equals("")&&deadline.equals(""))) {

            Form form = new Form(
                    user.getUid(),
                    subject.getText().toString(),
                    text.getText().toString(),
                    address.getText().toString(),
                    category_text.getSelectedItem().toString(),
                    cost.getText().toString(),
                    max_count.getText().toString(),
                    deadline.getText().toString(),
                    getTime1()
                );
                FID=Integer.toString(i++);
                database.child("form").push().setValue(form)
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


}