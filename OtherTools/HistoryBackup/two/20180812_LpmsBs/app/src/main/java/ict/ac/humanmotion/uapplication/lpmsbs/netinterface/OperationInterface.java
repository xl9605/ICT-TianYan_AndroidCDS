package ict.ac.humanmotion.uapplication.lpmsbs.netinterface;

import ict.ac.humanmotion.uapplication.lpmsbs.model.UserModel;
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

public interface OperationInterface {
    //GET请求
    @GET("operation")
    @FormUrlEncoded
        Call<Op> getOperation(@Field("userID") String userID, @Field("password") String password);
    //POST请求
    @POST("operation")
    @FormUrlEncoded
    Call<UserModel> postOperation(@Field("userID") String userID, @Field("password") String password);
    //GET（id）请求
    @GET("operation/id")
    @FormUrlEncoded
    Call<UserModel> getOperationByID(@Field("userID") String userID, @Field("password") String password);
    //PUT请求
    @PUT("operation/id")
    @FormUrlEncoded
    Call<UserModel> putOperationByID(@Field("userID") String userID, @Field("password") String password);
    //GET请求
    @DELETE("operation/id")
    @FormUrlEncoded
    Call<UserModel> deleteOperationByID(@Field("userID") String userID, @Field("password") String password);
}
