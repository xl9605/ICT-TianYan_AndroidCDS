package ict.ac.humanmotion.uapplication.lpmsbs.netinterface
import ict.ac.humanmotion.uapplication.lpmsbs.model.Operation
import ict.ac.humanmotion.uapplication.lpmsbs.model.User
import ict.ac.humanmotion.uapplication.lpmsbs.model.Worktable
import retrofit2.http.*
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.util.*

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/11.
 */

interface CloudAPI {

    //get all User
    @GET("user")
    fun getUser(@Query("offset") offset: Int = 0,
                   @Query("query") query: String? = null,
                   @Query("sortby") sortby: String? = null,
                   @Query("order") order: String? = null): Call<List<User>>

    //get User by id
    @GET("user/{id}")
    fun getUserById(@Path("id") UserId: Int): Call<User>

    //create User
    @POST("user")
    fun postUser(@Body User: User): Call<User>

    //update the User
    @PUT("user/{id}")
    fun putUserById(@Path("id") UserId: Int, @Body User: User): Call<User>

    //delete the User
    @DELETE("user/{id}")
    fun deleteUserById(@Path("id") UserId: Int): Call<User>

    //--------------------------------------------------------------------------------------------//

    //get all operation
    @GET("operation")
    fun getOperation(@Query("limit") limit: Int = 10,
                      @Query("offset") offset: Int = 0,
                      @Query("query") query: String? = null,
                      @Query("sortby") sortby: String? = null,
                      @Query("order") order: String? = null): Call<List<Operation>>

    //get enrollment by id
    @GET("operation/{id}")
    fun getOperationById(@Path("id") enrollmentId: Int): Call<Operation>

    //create Enrollment
    @POST("operation")
    fun postOperation(@Body enrollment: Observable): Call<String>

    //update the Enrollment
    @PUT("operation/{id}")
    fun putOperationById(@Path("id") enrollmentId: Int, @Body enrollment: Observable): Call<String>

    //delete the Enrollment
    @DELETE("operation/{id}")
    fun deleteOperationById(@Path("id") enrollmentId: Int): Call<String>

    //--------------------------------------------------------------------------------------------//

    //get all worktable
    @GET("worktable")
    fun getWorkTable(@Query("offset") offset: Int = 0,
                  @Query("query") query: String? = null,
                  @Query("sortby") sortby: String? = null,
                  @Query("order") order: String? = null): Call<List<Worktable>>

    //get course by id
    @GET("worktable/{id}")
    fun getWorkTableById(@Path("id") courseId: Int): Call<Worktable>

    //create course
    @POST("worktable")
    fun postWorkTable(@Body course: Worktable): Call<String>

    //update the course
    @PUT("worktable/{id}")
    fun putWorkTableById(@Path("id") courseId: Int, @Body course: Worktable): Call<String>

    //delete the course
    @DELETE("worktable/{id}")
    fun deleteWorkTableById(@Path("id") courseId: Int): Call<String>
}
