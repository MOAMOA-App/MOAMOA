package com.example.moamoa.sv;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Retrofit_Intercase {
    //카테고리 출력
    @GET("/retrofit/category")
    Call<List<RCategory>> category(); //메소드 명 test << 자유롭게 지정, 통신에 영향 x

    //이미 사용된 이메일 체크
    @POST("/retrofit/email_check")
    Call<RUser> email_check(@Field("email") String email);

    //회원가입 정보 DB 추가
    @GET("/retrofit/register")
    Call<RUser> register(
            @Field("uname") String uname,
            @Field("email") String email,
            @Field("pw") String pw,
            @Field("nick") String nick,
            @Field("profile") String profile,
            @Field("type") String type
    ); //메소드 명 test << 자유롭게 지정, 통신에 영향 x

    //내정보 가져오기(미완)
    @POST("/retrofit/get_user")
    Call<RUser> get_user(@Field("uid") String uid);

    //게시글 정보 가져오기(미완)
    @POST("/retrofit/get_form")
    Call<RForm> get_form(@Field("fid") String fid);

    //게시글 생성하기(미완)
    @GET("/retrofit/create_form")
    Call<RForm> createform(
            @Field("fuid") String fuid,
            @Field("fcategory") int fcategory,
            @Field("subject") String subject,
            @Field("text") String text,
            @Field("maxnum") int maxnum,
            @Field("location") String location,
            @Field("deadline") String deadline
    );

    //사용자 정보 가져오기 ALL
    @POST("/retrofit/get_user")
    Call<Object> userlist();

    //게시글 정보 가져오기 ALL
    @POST("/retrofit/get_form")
    Call<Object> formlist();

    /*
    @GET("/retrofit/get")
    Call<ResponseBody> getFunc(@Query("data") String data);

    @FormUrlEncoded
    @POST("/retrofit/post")
    Call<ResponseBody> postFunc(@Field("data") String data);

    @FormUrlEncoded
    @PUT("/retrofit/put/{id}")
    Call<ResponseBody> putFunc(@Path("id") String id, @Field("data") String data);

    @DELETE("/retrofit/delete/{id}")
    Call<ResponseBody> deleteFunc(@Path("id") String id);
     */

}
