package com.example.moamoa.sv;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Retrofit_Intercase {
    //카테고리 출력
    @GET("/retrofit/category")
    Call<RCategory> category(); //메소드 명 test << 자유롭게 지정, 통신에 영향 x

    //이미 사용된 이메일 체크
    @POST("/retrofit/email_check")
    Call<RUser> email_check(@Query("email") String email);

    //회원가입 정보 DB 추가
    @GET("/retrofit/register")
    Call<RUser> register(
            @Query("uname") String uname,
            @Query("email") String email,
            @Query("pw") String pw,
            @Query("nick") String nick,
            @Query("profile") String profile,
            @Query("type") String type
    ); //메소드 명 test << 자유롭게 지정, 통신에 영향 x

    //내정보 가져오기(미완)
    @POST("/retrofit/get_user")
    Call<RUser> get_user(@Query("uid") String uid);

    //게시글 정보 가져오기(미완)
    @POST("/retrofit/get_form")
    Call<RForm> get_form(@Query("fid") String fid);

    //게시글 생성하기(미완)
    @GET("/retrofit/create_form")
    Call<RForm> createform(
            @Query("fuid") String fuid,
            @Query("fcategory") int fcategory,
            @Query("subject") String subject,
            @Query("text") String text,
            @Query("maxnum") int maxnum,
            @Query("location") String location,
            @Query("deadline") String deadline
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
