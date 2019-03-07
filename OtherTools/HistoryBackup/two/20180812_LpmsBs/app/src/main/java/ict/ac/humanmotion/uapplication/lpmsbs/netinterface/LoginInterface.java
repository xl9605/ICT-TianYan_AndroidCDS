package ict.ac.humanmotion.uapplication.lpmsbs.netinterface;

import ict.ac.humanmotion.uapplication.lpmsbs.model.User;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/8/10 0010.
 */

public interface LoginInterface {
    //GET请求
    @GET("user")
    @FormUrlEncoded
    Call<User> getUserLogin(@Query("userID") int userID,);
    //POST请求
    @POST("user")
    @FormUrlEncoded
    Call<User> postUserLogin(@Field("userID") String userID,@Field("password") String password);
    //GET（id）请求
    @GET("user/{id}")
    @FormUrlEncoded
    Call<User> getUserLoginByID(@Field("userID") int userID);
    //PUT请求
    @PUT("user/id")
    @FormUrlEncoded
    Call<User> putUserLoginByID(@Field("userID") int userID);
    //GET请求
    @DELETE("user/id")
    @FormUrlEncoded
    Call<User> deleteUserLoginByID(@Field("userID") int userID);
}
