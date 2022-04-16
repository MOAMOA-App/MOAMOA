package com.example.moamoa.ui.mypage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MypageViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MypageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");

    }

   // public LiveData<String> getText() { return mText;}
//주석

}