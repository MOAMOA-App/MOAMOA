package com.example.moamoa.ui.formdetail;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentDashboardBinding;
import com.example.moamoa.databinding.FragmentFormstateBinding;
import com.example.moamoa.ui.dashboard.DashboardViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class FormStageFragment extends Fragment {
    private FragmentFormstateBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormstateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String FID;
        Bundle bundle = getArguments();
        if (bundle != null) {
            FID = bundle.getString("FID");
        }
        TextView state1 = (TextView)root.findViewById(R.id.formstate_1);
        TextView state2 = (TextView)root.findViewById(R.id.formstate_2);
        View line1 = (View)root.findViewById(R.id.formstate_line1);
        View line2 = (View)root.findViewById(R.id.formstate_line2);

        /*
        mDatabase.child("form").child(FID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.e("task",task.getResult().getValue().toString());
                /*
                int state = (int) task.getResult().child("state").getValue();
                if(state==1){
                    state1.setBackgroundColor(getResources().getColor(R.color.second_green));
                    line1.setBackgroundColor(getResources().getColor(R.color.second_green));
                }else if(state==2){
                    state2.setBackgroundColor(getResources().getColor(R.color.main_orange));
                    line2.setBackgroundColor(getResources().getColor(R.color.main_orange));
                }

            }
        });
        */
        return root;
    }
}