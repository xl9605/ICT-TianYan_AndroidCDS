package ict.ac.humanmotion.uapplication.lpmsbs.ui;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ict.ac.humanmotion.uapplication.lpmsbs.model.User;
import ict.ac.humanmotion.uapplication.lpmsbs.netinterface.LoginInterface;
import ict.ac.humanmotion.uapplication.lpmsbs.tools.CircularImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity{
    private EditText userID;
    private EditText password;
    private String userName;
    private String passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
       // getSupportActionBar().hide();
        CircularImage plogin = (CircularImage) findViewById(R.id.cover_user_photo);
        userID = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        Button ulogin=(Button)findViewById(R.id.ulogin);
        ulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                // TODO Auto-generated method stub
                userName = userID.getText().toString();
                passwd = password.getText().toString();
                if (passwd.length() == 0 || userName.length() == 0) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    request(userName,passwd);
                }
            }
        });
        plogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                // TODO Auto-generated method stub
                    login();
            }
        });
    }
    public void request(String userID,String passwd) {
        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.41.0.133:8080/v1/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();
        // 步骤5:创建 网络请求接口 的实例
        LoginInterface request = retrofit.create(LoginInterface.class);
        //对 发送请求 进行封装(设置需要翻译的内容)
        Call<User> call = request.postUserLogin(userID,passwd);
        //Call<Translation1> call = request.getCall();
        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<User>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // 请求处理,输出结果
                // 输出翻译的内容
              //  System.out.println("翻译是："+ response.body().getTranslateResult().get(0).get(0).getTgt());
           //     login();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivityForResult(intent, 15);
            }
            //请求失败时回调
            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                System.out.println("请求失败");
                System.out.println(throwable.getMessage());
                Toast.makeText(LoginActivity.this,"请求失败:"+throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void login() {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivityForResult(intent, 15);
    }
}