package com.example.windows.insta.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.windows.insta.Clientpackage.UserService;
import com.example.windows.insta.MainActivity;
import com.example.windows.insta.R;
import com.example.windows.insta.model.File_info;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Create_PostActivity extends AppCompatActivity {
    SharedPreferences preferences;
    String token ="";
    String url="";
    String ImagePath;
    ImageView img;
    private int PICK_IMAGE_REQUEST = 1;
    EditText image_description;
    boolean prest=false;

    Button choose,uplode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__post);
        url=getResources().getString(R.string.ip_photo);

        choose =findViewById(R.id.image_btn);
        uplode=findViewById(R.id.uplode);
        img = findViewById(R.id.img);
        image_description =findViewById(R.id.image_text);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},121);
        preferences =getSharedPreferences("sharedpref",MODE_PRIVATE);
        token =preferences.getString("Token","");
        token = "Bearer " + token.replaceAll("\\s+","");



        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
            }
        });

        uplode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prest){
                    uploadImage();
                }else {
                    Toast.makeText(getApplicationContext(),"Choose e image" ,Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void uploadImage(){
        File file = new File(ImagePath);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image",file.getName(),requestBody);
        String ipadd=getResources().getString(R.string.ip);

        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(ipadd)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserService user = retrofit.create(UserService.class);
        Call<ResponseBody> call = user.uplode(token,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String  result = response.body().string();
                        if (image_description.getText().toString().equals("")
                                || image_description.getText().toString() == null
                                || image_description.getText().toString().length() == 0 ){
                            Toast.makeText(getApplicationContext(),"Write a description",Toast.LENGTH_SHORT).show();
                        }else{
                            insertPost(result);
                        }

                    }catch (Exception e){

                    }
                }
               else  if (!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Error: " + response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failur" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertPost(String image_name){
        File_info file_info = new File_info(image_description.getText().toString(),image_name);
        String ipadd=getResources().getString(R.string.ip);
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(ipadd)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UserService user = retrofit.create(UserService.class);

        Call<ResponseBody> call = user.inserPost(token,file_info);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                    prest=false;
                }
                else if (!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Error: "+response.code()  ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failure" ,Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode==RESULT_OK){
            Uri imageuri = data.getData();
            ImagePath = getpath(imageuri);

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);

                img.setImageBitmap(bitmap);

            }catch (Exception e){

            }

            prest=true;

        }
    }






    private String getpath(Uri uri){
        String [] projection = {MediaStore.Images.Media.DATA};
        CursorLoader laoder = new CursorLoader
                (getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor = laoder.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                return true;
            case R.id.profile:
                Intent profil =new Intent(getApplicationContext(),ProfilActivity.class);
                startActivity(profil);
                return true;
            case R.id.create_post:
                startActivity(new Intent(getApplicationContext(),Create_PostActivity.class));
                return true;
            case R.id.logout:
                logout();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void logout(){
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.apply();

    }
}
