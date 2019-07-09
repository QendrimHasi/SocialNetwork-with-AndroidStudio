package com.example.windows.insta.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows.insta.Clientpackage.UserService;
import com.example.windows.insta.MainActivity;
import com.example.windows.insta.R;
import com.example.windows.insta.adapters.Custom_list_adapters;
import com.example.windows.insta.adapters.DataAdapter;
import com.example.windows.insta.model.ImageUrl;
import com.example.windows.insta.model.Post;
import com.example.windows.insta.model.PostOfFollowers;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String token;
    EditText textView;
    String url="";
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    String [] imglist = new String[50];
    String [] imgdescription = new String[50];
    int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},121);

        preferences =getSharedPreferences("sharedpref",MODE_PRIVATE);
         token =preferences.getString("Token","");
         url = getResources().getString(R.string.ip_photo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_home);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
           getProfilUser();

    }

    public void getProfilUser(){

        String ipadd=getResources().getString(R.string.ip);

        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(ipadd)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserService user = retrofit.create(UserService.class);

        Call<List<PostOfFollowers>> call=user.home("Bearer " + token.replaceAll("\\s+",""),0,5);

        call.enqueue(new Callback<List<PostOfFollowers>>() {
            @Override
            public void onResponse(Call<List<PostOfFollowers>> call, Response<List<PostOfFollowers>> response) {


                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Error: "+response.code(),Toast.LENGTH_LONG).show();
                }


                if (response.isSuccessful()) {
                    List<PostOfFollowers> postOfFollowers = response.body();
                    for (PostOfFollowers post:postOfFollowers){
                        imglist[count]="" + (url+post.getImg()).replaceAll("\\s+","");
                        imgdescription[count]=""+ post.getDescription();
                        count++;
                    }

                    ArrayList imageUrlList = prepareData();
                    Custom_list_adapters dataAdapter = new Custom_list_adapters(getApplicationContext(), imageUrlList);
                    recyclerView.setAdapter(dataAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<PostOfFollowers>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Toast.makeText(this,"You are at home",Toast.LENGTH_LONG).show();
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

    private ArrayList prepareData() {
        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ImageUrl imageUrl = new ImageUrl(imglist[i],imgdescription[i]);
            imageUrlList.add(imageUrl);
        }
        return imageUrlList;
    }
}
