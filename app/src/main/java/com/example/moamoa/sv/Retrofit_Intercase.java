package com.example.moamoa.sv;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Retrofit_Intercase {
    @GET("/retrofit/category")
    Call<RCategory> category(); //메소드 명 test << 자유롭게 지정, 통신에 영향 x

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
