package com.example.windows.insta;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.windows.insta.Activity.HomeActivity;
import com.example.windows.insta.Clientpackage.UserService;
import com.example.windows.insta.model.ResObj;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText usernameInput,passwordInput;
    String username,password;
    Button login;

    private  final String sharedpref="sharedpref";

    private int prmision= 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},prmision);

        usernameInput=findViewById(R.id.Usernameinput);
        passwordInput=findViewById(R.id.passwordinput);
        login=findViewById(R.id.btSignIn);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=usernameInput.getText().toString();
                password=passwordInput.getText().toString();
                if (validateLogin(username,password)){
                    doLogin(username,password);
                }
            }
        });
    }


    private boolean validateLogin(String username,String password){
        if (username==null || username.trim().length()==0 ){
            Toast.makeText(this,"Username is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password==null || password.trim().length()==0 ){
            Toast.makeText(this,"Password is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() { }

    private void doLogin(String username, String password){
        String ipadd=getResources().getString(R.string.ip);

        ResObj resObj = new ResObj(username,password);
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(ipadd)
                .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();
         UserService user = retrofit.create(UserService.class);
         Call<ResponseBody> call = user.login(resObj);

         call.enqueue(new Callback<ResponseBody>() {
             @Override
             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                 if (!response.isSuccessful()){

                     Toast.makeText(MainActivity.this,"Invalid Login "+response.code() ,Toast.LENGTH_LONG).show();
                     return;
                 }
                 if (response.isSuccessful()) {
                     String ans=response.headers().toString();
                     String [] a = ans.split("X-Content-Type-Options");
                     String [] token =a[0].split(" ");
                    // Toast.makeText(MainActivity.this, ""+token[1]+" "+token[2],Toast.LENGTH_LONG).show();
                     SharedPreferences preferences = getSharedPreferences(sharedpref,MODE_PRIVATE);
                     SharedPreferences.Editor editor = preferences.edit();
                     editor.putString("Token",token[2]);
                     editor.commit();

                     Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                    // i.putExtra("token",token[2]);
                     startActivity(i);
                 }
             }

             @Override
             public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this,"failure",Toast.LENGTH_LONG).show();
             }
         });

    }


}
