package com.example.moamoa.ui.formdetail;

import com.google.firebase.database.DatabaseReference;

public class Detail_Function {
    private static DatabaseReference mDatabase;
    /*
    public static ArrayList getCategory(){
        ArrayList<String> CATEGORY = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //변화된 값이 DataSnapshot 으로 넘어온다.
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    Log.e("", fileSnapshot.getValue().toString());
                    CATEGORY.add(fileSnapshot.getValue().toString());
                    //Ctgy.add(fileSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
     */
}
