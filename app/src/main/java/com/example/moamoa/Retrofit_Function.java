package com.example.moamoa;

import android.util.Log;

import com.example.moamoa.sv.RCategory;
import com.example.moamoa.sv.RForm;
import com.example.moamoa.sv.RUser;
import com.example.moamoa.sv.Retrofit_Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Retrofit_Function {
    //카테고리 불러오기
    public void category(){
        Call<RCategory> call;
        call = Retrofit_Client.getApiService().category();
        call.enqueue(new Callback<RCategory>() {
            @Override
            public void onResponse(Call<RCategory> call, Response<RCategory> response) {
                RCategory result = response.body();
                Log.e("Retrofit:connect -> ", result.getCategory().toString());
            }

            @Override
            public void onFailure(Call<RCategory> call, Throwable t) {
                Log.e("Retrofit:error -> ", t.toString());
            }
        });
    }

    public static void userlist(){//error
        Call<Object> call;
        call = Retrofit_Client.getApiService().userlist();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> RUser, Response<Object> response) {
                Object result = response.body();
                Log.e("Retrofit:connect -> ", String.valueOf(result.getClass()));
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("Retrofit:error -> ", t.toString());
            }
        });
    }

    //이미 있는 이메일인지 확인
    public void email_check(String email){
        Call<RUser> call;
        call = Retrofit_Client.getApiService().email_check(email);
        call.enqueue(new Callback<RUser>() {
            @Override
            public void onResponse(Call<RUser> call, Response<RUser> response) {
                RUser result = response.body();
                Log.e("Retrofit:connect -> ", result.getNow().toString());
            }

            @Override
            public void onFailure(Call<RUser> call, Throwable t) {
                Log.e("Retrofit:error -> ", t.toString());
            }
        });
    }
    //회원가입하기
    public static void register(String uname, String email, String passwd, String nick, String profile, String type){
        Call<RUser> call;
        call = Retrofit_Client.getApiService().register(uname, email, passwd, nick, profile, type);
        call.enqueue(new Callback<RUser>() {
            @Override
            public void onResponse(Call<RUser> call, Response<RUser> response) {
                RUser result = response.body();
                Log.e("Retrofit:connect -> ","insert clear");
            }

            @Override
            public void onFailure(Call<RUser> call, Throwable t) {
                Log.e("Retrofit:error -> ", t.toString());
            }
        });
    }
    //게시글 생성하기
    public static void create_form(String uid, int category, String subject, String text, int max, String location, String deadline){
        Call<RForm> call;
        call = Retrofit_Client.getApiService().createform(uid, category, subject, text, max, location, deadline);

        call.enqueue(new Callback<RForm>() {
            @Override
            public void onResponse(Call<RForm> call, Response<RForm> response) {
                RForm result = response.body();
                Log.e("Retrofit:connect -> ","create_form");
            }

            @Override
            public void onFailure(Call<RForm> call, Throwable t) {
                Log.e("Retrofit:error -> ", t.toString());
            }
        });
    }




}
