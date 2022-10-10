package com.example.moamoa.ui.formdetail;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.moamoa.R;
import com.example.moamoa.ui.chats.ChatModel;
import com.example.moamoa.ui.chats.ChatsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PartyDialog extends DialogFragment {
    private Fragment fragment;
    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    private View view;
    private ArrayList<PartyListData> partylist=new ArrayList();
    String FID;

    private List<ChatModel> chatModels = new ArrayList<>();
    private ArrayList<String> JoinedUsers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view            = inflater.inflate(R.layout.partylist,container,false);
        firebaseStorage = FirebaseStorage.getInstance();
        mDatabase       = FirebaseDatabase.getInstance().getReference();

        Bundle mArgs = getArguments();
        FID = mArgs.getString("FID");
        if (fragment != null) {
            DialogFragment dialogFragment = (DialogFragment) fragment;
            dialogFragment.dismiss();
        }
        InitializePartiform();
        Button close_btn = (Button) view.findViewById(R.id.partylist_closebtn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartyDialog.this.dismiss();
            }
        });

        return view;
    }

    public void InitializePartiform()
    {
        TextView partynumber = (TextView) view.findViewById(R.id.partylist_number);
        ListView listView = (ListView)view.findViewById(R.id.partylist_view) ;

        mDatabase.child("party").child(FID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int count = (int) task.getResult().getChildrenCount();
                final int[] x = {0};
                partynumber.setText(count+"");
                for(DataSnapshot dataSnapshot : task.getResult().getChildren()){
                    PartyListData listdata = new PartyListData();
                    String uid = dataSnapshot.getKey()+"";
                    String date = dataSnapshot.getValue()+"";

                    // 유저 목록에 uid 추가 (채팅창 이동에 사용)
                    JoinedUsers.add(uid);

                    // 마지막채팅출력을위한...밑준비
                    /*
                    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mDatabase.child("chatrooms").orderByChild("users/"+UID).equalTo(true)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    chatModels.clear();
                                    for (DataSnapshot item : snapshot.getChildren()){
                                        ChatModel chatModel = item.getValue(ChatModel.class); //채팅방 아래 데이터 가져옴
                                        // uid 있는 방 있으면 chatModels에
                                        if (chatModel.users.containsKey(uid)){
                                            chatModels.add(item.getValue(ChatModel.class));
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                     */

                    mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            listdata.setUID(uid);
                            listdata.setNickname(task.getResult().child("nick").getValue()+"");
                            listdata.setParti_date(date.substring(4,6)+"/"+date.substring(6,8)+" "+date.substring(8,10)+":"+date.substring(10,12));
                            listdata.setProfile(task.getResult().child("image").getValue()+"");

                            listdata.setLastchat("");

                            partylist.add(listdata);
                            x[0]++;
                            if(x[0] ==count){
                                partynumber.setText(count+"");
                                PartyListAdapter myAdapter = new PartyListAdapter(getContext(),partylist);
                                listView.setAdapter(myAdapter);
                            }

                        }
                    });
                }

                // 일단 챗모델이랑 내 UID 불러옴 -> 내가 들어있는 방 검색
                // -> destinationUsers에 있는 사람 뽑아옴 (CLF에서 //채팅방에 있는 유저 뽑아옴 이부분)
                // -> 그다음에 내림차순 정렬 후 마지막 메시지 뽑아옴 일케하면될듯
                // 아 너무 번잡시러울거같은데 어케... 함수로 만들수잇는부분은 만들어야될듯
                // position 어케쓰지 쓰으읍
                /*
                Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
                commentMap.putAll(chatModels.get(position).comments);
                String lastMessageKey = (String) commentMap.keySet().toArray()[0];
                listdata.setLastchat(chatModels.get(position).comments.get(lastMessageKey).message);

                 */

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(view.getContext(), ChatsActivity.class);

                        intent.putExtra("destinationUID", JoinedUsers.get(position));
                        intent.putExtra("FID", FID);

                        startActivity(intent);
                    }
                });

            }
        });
    }
}
