package com.example.moamoa.ui.dashboard;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moamoa.Form;
import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentDashboardBinding;
import com.example.moamoa.ui.account.User;
import com.example.moamoa.ui.category.CategoryActivity;
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
    long mNow;
    Date mDate;
    boolean on = false;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat mFormat1 = new SimpleDateFormat("yyyyMMddhhmmss");
    StorageReference storageRef;
    StorageReference riversRef;
    FirebaseDatabase firebaseDatabase;
    UploadTask uploadTask;
    Uri file;
    String FID;
    int num_a;
    String max;
    String express;
    int photo_num;
    ImageView photo;
    String imageUrl;
    Button button_img;
    private FirebaseStorage storage;
    private final int GALLERY_CODE = 10;
    private FirebaseAuth firebaseAuth;
    int i = 1;
    String point;
    ClipData clipData;
    String address_s;
    private static final String TAG = "MultiImageActivity";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    EditText address;

    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터

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

        EditText subject    = (EditText) root.findViewById(R.id.subject);               //제목
        TextView today      = (TextView) root.findViewById(R.id.text_dashboardstart);   //게시글 생성 날짜
        EditText text       = (EditText) root.findViewById(R.id.text);                  //내용
        address             = (EditText) root.findViewById(R.id.address);               //주소
        EditText cost       = (EditText) root.findViewById(R.id.cost);                  //금액
        EditText max_count  = (EditText) root.findViewById(R.id.max_count);             //마감 최대 인원 수
        Button button       = (Button) root.findViewById(R.id.button_dashboard);        //
        Button btn_addr     = (Button) root.findViewById(R.id.button_addr);             //
        TextView deadline   = (TextView) root.findViewById(R.id.text_dashboardend);     //마감일자
        CheckBox checkBox   = (CheckBox) root.findViewById(R.id.checkBox);              //
        RadioGroup radioGroup   = (RadioGroup) root.findViewById(R.id.radioGroup);      //거래 방식
        Spinner category_text   = (Spinner) root.findViewById(R.id.spinner);            //카테고리

        today.setText(getTime1().substring(0,4)+"/"+getTime1().substring(4,6)+"/"+getTime1().substring(6,8));
        cost.addTextChangedListener(new CustomTextWatcher(cost));
        photo = (ImageView) root.findViewById(R.id.imageView);
        storage = FirebaseStorage.getInstance();


        Button btn_getImage = root.findViewById(R.id.button_imgs);
        btn_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });
        Log.d("확인","message : "+checkBox.isChecked());
        recyclerView = root.findViewById(R.id.recyclerView);

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton1:
                        express= "직거래";
                        break;
                    case R.id.radioButton2:
                        express= "택배";
                        break;
                    case R.id.radioButton3:
                        express= "둘다가능";
                        break;
                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if (checkBox.isChecked()) {
                                              max_count.setClickable(false);
                                              max_count.setFocusable(false);
                                              max= "100";
                                              max_count.setVisibility(View.GONE);

                                          } else {
                                              max="";
                                              max_count.setVisibility(View.VISIBLE);
                                              max_count.setFocusableInTouchMode(true);
                                              max_count.setFocusable(true);
                                          }
                                      }

 });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);

            }
        });
        btn_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (on == false){
                    Toast.makeText(getContext(),"사진을 추가하세요",Toast.LENGTH_SHORT).show();
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
                if (address.getText().toString().length()==0){
                    Toast.makeText(getContext(),"주소를 입력하세요",Toast.LENGTH_SHORT).show();
                    address.requestFocus();
                    return;
                }
                if (cost.getText().toString().length()==0){
                    Toast.makeText(getContext(),"가격을 입력하세요",Toast.LENGTH_SHORT).show();
                    cost.requestFocus();
                    return;
                }
                if (express.length()==0){
                    Toast.makeText(getContext(),"배송 여부를 선택하세요요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (max_count.getText().toString().length()==0 && !checkBox.isChecked() ){
                    Toast.makeText(getContext(),"모집인원을 입력하세요",Toast.LENGTH_SHORT).show();
                    max_count.requestFocus();
                    return;

                }
                if (deadline.getText().toString().length()==0){
                    Toast.makeText(getContext(),"마감일자를 입력하세요",Toast.LENGTH_SHORT).show();
                    deadline.requestFocus();
                    return;

                }
                FID=user.getUid()+num_a;
//if (!(subject.equals("") && text.equals("") &&address.equals("")&&cost.equals("")&&max_count.equals("")&&deadline.equals(""))) {
                max=max_count.getText().toString();
                String dead = (deadline.getText().toString().substring(0,4))+deadline.getText().toString().substring(5,7)+deadline.getText().toString().substring(8,10).toString();
                Form form = new Form(
                        FID,
                        user.getUid(),
                        "photo/"+FID+".png",
                        photo_num,
                        subject.getText().toString(),
                        text.getText().toString(),
                        address.getText().toString(),
                        1,
                        Integer.parseInt(cost.getText().toString().replace(",","")),
                        max,
                        Integer.parseInt(dead),
                        getTime1(),
                        0,
                        express,
                        point
                );


                Log.i("num",num_a+"");
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("num").setValue(num_a);
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child(FID).setValue("host");


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users");


               // childUpdates.put(user.getUid(), postValues);
               // reference.updateChildren(childUpdates);

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
    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getArguments() != null)
        {
            address_s = getArguments().getString("address"); // 프래그먼트1에서 받아온 값 넣기
            address.setText(address_s);
        }
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