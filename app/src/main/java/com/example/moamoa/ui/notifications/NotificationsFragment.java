package com.example.moamoa.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private ArrayList<NotificationsData> list = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Form listData;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.FormData();
        //ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.notification_list);

        recyclerView.setHasFixedSize(true);
        adapter = new NotificationsAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return root;

    }

    public void FormData()
    {
        list = new ArrayList<NotificationsData>();

        //생성한 유저 계정에 표시
        FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Form listData = snapshot.getValue(Form.class);
                    Log.d("확인","루트 : "+user.getUid());

                    if (listData.UID_dash.equals(user.getUid())&& listData.getstate()==1) {
                        list.add(new NotificationsData("모든 인원이 참여하였습니다. 거래를 진행해주세요.",  listData.getSubject()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //참여한 유저의 계정에 알림 표시
        FirebaseDatabase.getInstance().getReference().child("party").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
//                list.clear();

                FirebaseDatabase.getInstance().getReference("form").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            listData = snapshot.getValue(Form.class);

                            if (dataSnapshot2.child(listData.FID).child(user.getUid()).getValue()!=null) {
                                Log.d("확인","루트 : "+dataSnapshot2.child(user.getUid()).child(listData.FID).getKey().toString());

                                if (listData.getstate() == 1) {
                                    list.add(new NotificationsData("거래가 성사되어 진행중입니다.", dataSnapshot2.child(user.getUid()).child(listData.FID).getKey().toString()));
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        list.add(new NotificationsData("참여한 폼에 변동 사항이 있습니다.", "폼이름3"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}