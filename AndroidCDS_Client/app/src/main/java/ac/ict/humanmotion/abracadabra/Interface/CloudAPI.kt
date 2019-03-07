package ac.ict.humanmotion.abracadabra.Interface

import ac.ict.humanmotion.abracadabra.Bean.Operation
import ac.ict.humanmotion.abracadabra.Bean.User
import ac.ict.humanmotion.abracadabra.Bean.Worktable
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*



/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/11.
 */

interface CloudAPI {
   /* @FormUrlEncoded
    @POST("save/{label}")
    fun postSave(@Path("label") label: String, @Field("result") str: String): Call<String>*/
   //create Enrollment
   @POST("operation")
   fun postOperation(@Body enrollment: Operation): Observable<String>

    @Multipart
    @POST("ocr/{name}")
    fun uploadOCR(@Path("name") label: String,@Part file: MultipartBody.Part): Observable<String>

    @Multipart
    @POST("face")
    fun uploadFace(@Part file: MultipartBody.Part): Observable<String>

    @FormUrlEncoded
    @POST("predict/{name}")
    fun postPredict(@Path("name") label: String,@Field("content") str: String): Call<String>



    @Headers("Content-Type: application/json","Accept: application/json")//需要添加头
    @POST ("robot/{name}")
    fun postJson(@Path("name") label: String,@Body requestBody: RequestBody):Call<ResponseBody> // 请求体味RequestBody"}

    // TODO: F1-> POST IMAGE TO SERVER

    // TODO: F2-> POST IMAGE TO SERVER and GET CAMPARE RESULT

    // TODO: F3-> GET OCR RESULT

    // TODO: F4-> POST LMPSB2 DATA

}
