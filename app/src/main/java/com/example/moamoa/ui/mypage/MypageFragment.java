package com.example.moamoa.ui.mypage;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.moamoa.LoginActivity;
import com.example.moamoa.R;
import com.example.moamoa.ui.account.User;
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

//import com.example.moamoa.User;

public class MypageFragment extends Fragment {

    static final String[] LIST_MENU1 = {"✎  만든 폼 관리", "✔  참여한 폼 관리", "♡  관심 목록"} ;
    static final String[] LIST_MENU2 = {"\uD83D\uDC64  내 정보 수정", "⚙  환경설정"} ;
    private DatabaseReference mDatabase;
    private CustomDialog customDialog;
    private Nickname nickn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_page, null) ;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageView mainImage = view.findViewById(R.id.profile_image);
        TextView nickname = view.findViewById(R.id.nickname);
        TextView id = view.findViewById(R.id.ID);
        TextView area = view.findViewById(R.id.area);




        if (user != null) {
            // User is signed in
            // Name, email address, and profile photo Url
            //프로필 정보

            //nikname은 auth(계정 정보)에 들어가지 않으므로 database getReference()를 이용
            //닉네임 뜨는데 딜레이가 있음
            mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //닉네임 표시
                    User User = snapshot.getValue(User.class);
                    nickname.setText(User.getnick());

                    //이미지 표시
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();
                    StorageReference pathReference = storageReference.child("profile");

                    String dimage = snapshot.child("image").getValue().toString();
                    StorageReference submitProfile = storageReference.child(dimage);
                    submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(mainImage.getContext())
                                    .load(uri)
                                    .into(mainImage);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"데이터를 가져오는데 실패했습니다" , Toast.LENGTH_LONG).show();
                        }
                    });
                    //

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                    Toast.makeText(getContext(),"데이터를 가져오는데 실패했습니다" , Toast.LENGTH_LONG).show();
                }
            });
            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();

            id.setText(email);
//        area.setText(listViewData.get(position).charge);

        } else {
            // No user is signed in
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        //닉네임 수정 popup
        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView 클릭될 시 할 코드작성
                nickn = new Nickname(getContext(),"다이어로그에 들어갈 내용입니다.");
                nickn.show();
            }
        });

//        마이페이지 프로필 이미지 수정 popup
        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomDialog(getContext(),"다이어로그에 들어갈 내용입니다.");
                customDialog.show();
            }
        });

        //게시글 관리 리스트
        ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU1){
            //리스트뷰 글씨색 바꾸기
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };

        //내 정보 설정
        ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU2) ;

        ListView listview1 = (ListView) view.findViewById(R.id.listView1) ;
        listview1.setAdapter(adapter1) ;

        ListView listview2 = (ListView) view.findViewById(R.id.listView2) ;
        listview2.setAdapter(adapter2) ;

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "메세지", Toast.LENGTH_SHORT).show();
                if(position == 0){
                    Intent intent = new Intent(getActivity(), CreatedForms.class);
                    startActivity(intent);
                }
                if(position == 1){
                    Intent intent = new Intent(getActivity(), EmptyForms.class);
                    startActivity(intent);
                }
                if(position == 2) {
                    Intent intent = new Intent(getActivity(), EditMyinfo.class);
                    startActivity(intent);
                }
            }
        });

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "메세지", Toast.LENGTH_SHORT).show();
                if(position == 0){
                    Intent intent = new Intent(getActivity(), EditMyinfo.class);
                    startActivity(intent);
                }
                if(position == 1){
                    Intent intent = new Intent(getActivity(), Setting.class);
                    startActivity(intent);
                }
            }
        });

        return view ;
    }

}


