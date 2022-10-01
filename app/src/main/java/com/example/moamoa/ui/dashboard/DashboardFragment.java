package com.example.moamoa.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.Form;
import com.example.moamoa.MainActivity;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentDashboardBinding;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.category.CategoryActivity;
import com.example.moamoa.ui.formdetail.FormdetailActivity;
import com.example.moamoa.ui.home.HomeFragment;
import com.example.moamoa.ui.mypage.Nickname;
import com.example.moamoa.ui.mypage.PageViewModel;
import com.example.moamoa.ui.mypage.PlaceholderFragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DashboardFragment extends Fragment {
    /**
     *공동구매 글 작성
     * **/
    private FragmentDashboardBinding binding;
    boolean on = false;
    StorageReference storageRef;
    StorageReference riversRef;
    UploadTask uploadTask;
    String FID;
    int num_a;
    int max;
    String express;
    String temp;
    int photo_num;
    ImageView photo;
    private FirebaseStorage storage;
    private final int GALLERY_CODE = 10;
    private FirebaseAuth firebaseAuth;
    int i = 1;
    ClipData clipData;
    String addr_co="";
    String addr_edit="";
    String addr_de_edit="";
    RecyclerView recyclerView;
    Button btn_image;

    private static final String TAG = "MultiImageActivity";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체


    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터

    private String GetTimeStart(){
        SimpleDateFormat mFormat1 = new SimpleDateFormat("yyyyMMddhhmmss");
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);

        return mFormat1.format(mDate);
    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText subject    = (EditText) root.findViewById(R.id.subject);               //제목
        TextView today      = (TextView) root.findViewById(R.id.today);                 //게시글 생성 날짜
        EditText text       = (EditText) root.findViewById(R.id.text);                  //내용
        TextView addr_search    = (TextView) root.findViewById(R.id.addr_search);       //주소
        EditText addr_detail    = (EditText) root.findViewById(R.id.addr_detail);       //상세주소
        EditText cost           = (EditText) root.findViewById(R.id.cost);              //금액
        EditText max_people     = (EditText) root.findViewById(R.id.max_people);        //마감 최대 인원 수
        Button btn_dashboard    = (Button) root.findViewById(R.id.btn_dashboard);       //글쓰기버튼
//        Button btn_addr         = (Button) root.findViewById(R.id.btn_addr);            //주소검색버튼
        TextView deadline       = (TextView) root.findViewById(R.id.deadline);          //마감일자
        CheckBox check_people   = (CheckBox) root.findViewById(R.id.check_people);      //인원제한유무
        CheckBox check_offline  = (CheckBox) root.findViewById(R.id.check_offline);     //거래 방식
        CheckBox check_online   = (CheckBox) root.findViewById(R.id.check_online);      //거래 방식
        btn_image = root.findViewById(R.id.btn_image);
        recyclerView =  root.findViewById(R.id.recyclerView);                           // 이미지를 보여줄 리사이클러뷰
        Spinner category   = (Spinner) root.findViewById(R.id.category);                //카테고리
        today.setText(GetTimeStart().substring(0,4)+"/"+GetTimeStart().substring(4,6)+"/"+GetTimeStart().substring(6,8));
        cost.addTextChangedListener(new CustomTextWatcher(cost));
        photo = (ImageView) root.findViewById(R.id.imageView);
        storage = FirebaseStorage.getInstance();

        addr_search.setClickable(true);
        addr_search.setFocusable(true);
        addr_detail.setFocusableInTouchMode(true);
        addr_detail.setFocusable(true);
//        btn_addr.setClickable(true);
//        btn_addr.setFocusable(true);

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1= snapshot.getValue(User.class);
                num_a=user1.getnum()+1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        FirebaseDatabase.getInstance().getReference("map").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    addr_co = snapshot.child("경위도").getValue().toString();
                    addr_search.setText( snapshot.child("주소").getValue().toString());

                    FirebaseDatabase.getInstance().getReference("map").child(user.getUid()).child("경위도").setValue(null);
                    FirebaseDatabase.getInstance().getReference("map").child(user.getUid()).child("주소").setValue(null);

                }catch (NullPointerException e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        check_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_people.isChecked()) {
                    max_people.setClickable(false);
                    max_people.setFocusable(false);
                    max= 100;
                } else {
                    max_people.setText("");

                    max_people.setFocusableInTouchMode(true);
                    max_people.setFocusable(true);
                }
            }

        });
        addr_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });
//        btn_addr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), MapActivity.class);
//                startActivity(intent);
//
//            }
//        });
        check_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_offline.isChecked() & !check_online.isChecked()) {

                    addr_search.setClickable(true);
                    addr_search.setFocusable(true);
                    addr_detail.setFocusableInTouchMode(true);
                    addr_detail.setFocusable(true);
//                    btn_addr.setClickable(true);
//                    btn_addr.setFocusable(true);
                } else if (check_offline.isChecked() & check_online.isChecked()) {

                    addr_search.setClickable(true);
                    addr_search.setFocusable(true);
                    addr_detail.setFocusableInTouchMode(true);
                    addr_detail.setFocusable(true);


                } else if (!check_offline.isChecked() & check_online.isChecked()) {

                    addr_search.setText("");
                    addr_detail.setText("");
                    addr_search.setClickable(false);
                    addr_search.setFocusable(false);
                    addr_detail.setClickable(false);
                    addr_detail.setFocusable(false);

                }
            }
        });
        check_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_offline.isChecked() & !check_online.isChecked()) {
                    addr_search.setClickable(true);
                    addr_search.setFocusable(true);
                    addr_detail.setFocusableInTouchMode(true);
                    addr_detail.setFocusable(true);

                } else if (check_offline.isChecked() & check_online.isChecked()) {
                    addr_search.setClickable(true);
                    addr_search.setFocusable(true);
                    addr_detail.setFocusableInTouchMode(true);
                    addr_detail.setFocusable(true);


                } else if (!check_offline.isChecked() & check_online.isChecked()) {
                    addr_search.setText("");
                    addr_detail.setText("");
                    addr_search.setClickable(false);
                    addr_search.setFocusable(false);
                    addr_detail.setClickable(false);
                    addr_detail.setFocusable(false);

                }else{

                    addr_search.setClickable(true);
                    addr_search.setFocusable(true);
                    addr_detail.setFocusableInTouchMode(true);
                    addr_detail.setFocusable(true);

                }
           }

        });
        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (on == false){
                    Toast.makeText(getContext(),"사진을 추가하세요",Toast.LENGTH_SHORT).show();
                    //Log.i("num",temp);
                    return;
                }
                if (subject.getText().toString().length()==0){
                    Toast.makeText(getContext(),"제목을 입력하세요",Toast.LENGTH_SHORT).show();
                    subject.requestFocus();
                    return;
                }

                if (text.getText().toString().length()==0){
                    Toast.makeText(getContext(),"내용을 입력하세요",Toast.LENGTH_SHORT).show();
                    text.requestFocus();
                    return;
                }
                if (addr_search.getText().toString().length()==0 & check_offline.isChecked() & !check_online.isChecked()){
                    Toast.makeText(getContext(),"주소를 입력하세요",Toast.LENGTH_SHORT).show();
                    addr_search.requestFocus();
                    return;
                }
                if (cost.getText().toString().length()==0){
                    Toast.makeText(getContext(),"가격을 입력하세요",Toast.LENGTH_SHORT).show();
                    cost.requestFocus();
                    return;
                }
                if (!check_offline.isChecked() & !check_online.isChecked()){
                    Toast.makeText(getContext(),"배송 여부를 선택하세요",Toast.LENGTH_SHORT).show();
                    check_offline.requestFocus();
                    return;
                }
                if (max_people.getText().toString().length()==0 && !check_people.isChecked() ){
                    Toast.makeText(getContext(),"모집인원을 입력하세요",Toast.LENGTH_SHORT).show();
                    max_people.requestFocus();
                    return;

                }
                if (deadline.getText().toString().length()==0){
                    Toast.makeText(getContext(),"마감일자를 입력하세요",Toast.LENGTH_SHORT).show();
                    deadline.requestFocus();
                    return;

                }



                FID=user.getUid()+num_a;

                if(max_people.getText().toString().equals("")){
                    max=1000;
                }
                else{

                    max=Integer.parseInt(max_people.getText().toString());
                }


//                if(addr_search.getText().toString().equals("")){
//                    addr_co="-";
//                    addr_edit="-";
//                }
//                if(addr_detail.getText().toString().equals("")){
//                    addr_de_edit="-";
//                }

                String dead = (deadline.getText().toString().substring(0,4))+deadline.getText().toString().substring(5,7)+deadline.getText().toString().substring(8,10);

                String[] cat = getResources().getStringArray(R.array.category);
                temp = (String) category.getSelectedItem();

                int category_int = 0;
                for (i=0;i<cat.length;i++) {
                    if(cat[i].equals(temp)) category_int=i;
                }
                String check_text = "";
                if(check_offline.isChecked()){
                    if(check_online.isChecked()){
                        check_text = "직거래/대면";
                    }else{
                        check_text = "직거래/택배";
                    }
                }else{
                    if(check_online.isChecked()){
                        check_text = "택배/비대면";
                    }
                }
                Form form = new Form(
                        FID,            //FID
                        user.getUid(),  //FUID
                        "photo/"+FID+".png",       //이미지
                        photo_num,                       //이미지 개수
                        subject.getText().toString(),    //제목
                        text.getText().toString(),       //내용
                        addr_search.getText().toString(),//주소
                        addr_co,//위경도
                        addr_de_edit,//상세주소
                        category_int,                    //카테고리 번호
                        Integer.parseInt(cost.getText().toString().replace(",","")),    //가격
                        max,                             //최대인원수
                        Integer.parseInt(dead),          //마감일자
                        GetTimeStart(),                  //시작일자
                        0,                         //조회수
                        check_text,                 //대면비대면
                        0                          //활성화 상태
                );



                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("num").setValue(num_a);
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child(FID).setValue("host");


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users");


                // childUpdates.put(user.getUid(), postValues);
                // reference.updateChildren(childUpdates)

                storageRef = storage.getReference();
                for(int i = 0; i < clipData.getItemCount(); i++) {
                    riversRef = storageRef.child("photo/" + user.getUid() + num_a + "_"+(i+1)+".png");

                    uploadTask = riversRef.putFile(uriList.get(i));
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
                }
                FirebaseDatabase.getInstance().getReference("form").child(FID).setValue(form)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(getContext(), "글쓰기 성공", Toast.LENGTH_SHORT).show();

                                Popup popup;
                                popup = new Popup(getContext(),FID,user.getUid());
                                popup.show();

                                //내용 삭제
                                subject.setText(null);
                                category.setAdapter(null);
                                text.setText(null);
                                cost.setText(null);
                                check_offline.setChecked(false);
                                check_online.setChecked(false);
                                max_people.setText(null);
                                check_people.setChecked(false);
                                addr_detail.setText(null);

                                //fragment replace(내용은 사라지지 않음/왜?)
//                                HomeFragment homeFragment = new HomeFragment();
                                FragmentManager manager = getFragmentManager();
                                FragmentTransaction trans = manager.beginTransaction();
//                                trans.add(R.id.nav_host,homeFragment).addToBackStack(null).commitAllowingStateLoss();
//                                trans.replace(R.id.navigation_dashboard, ).addToBackStack(null).commitAllowingStateLoss();
//                                NavHostFragment.findNavController(HomeFragment);
//                                NavController navController = Navigation.findNavController(navigation_home, R.id.nav_host);
//
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
    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getActivity(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();

                uriList.add(imageUri);
                photo_num=1;
                adapter = new MultiImageAdapter(uriList, getContext());
                recyclerView.setAdapter(adapter);
                // recyclerView.setItemAnimator(null);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
            }
            else{      // 이미지를 여러장 선택한 경우
                clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.
                            on=true;
                            photo_num= clipData.getItemCount();
                            btn_image.setText(photo_num+"/"+10);
                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList, getContext());
                    recyclerView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}