package ict.ac.humanmotion.uapplication.lpmsbs.netinterface;

import ict.ac.humanmotion.uapplication.lpmsbs.model.UserModel;
import ict.ac.humanmotion.uapplication.lpmsbs.model.Worktable;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Administrator on 2018/8/10 0010.
 */

public interface WorkTableInterface {
    //GET请求
    @GET("worktable")
    @FormUrlEncoded
    Call<Worktable> getWorkTable(@Field("userID") String userID, @Field("password") String password);
    //POST请求
    @POST("worktable")
    @FormUrlEncoded
    Call<Worktable> postWorkTable(@Field("userID") String userID, @Field("password") String password);
    //GET（id）请求
    @GET("worktable/id")
    @FormUrlEncoded
    Call<Worktable> getWorkTableByID(@Field("userID") String userID, @Field("password") String password);
    //PUT请求
    @PUT("worktable/id")
    @FormUrlEncoded
    Call<Worktable> putWorkTableByID(@Field("userID") String userID, @Field("password") String password);
    //GET请求
    @DELETE("worktable/id")
    @FormUrlEncoded
    Call<Worktable> deleteWorkTableByID(@Field("userID") String userID, @Field("password") String password);
}
