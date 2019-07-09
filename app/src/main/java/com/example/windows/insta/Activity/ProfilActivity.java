package com.example.windows.insta.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows.insta.Clientpackage.UserService;
import com.example.windows.insta.MainActivity;
import com.example.windows.insta.R;
import com.example.windows.insta.adapters.DataAdapter;
import com.example.windows.insta.model.ImageUrl;
import com.example.windows.insta.model.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfilActivity extends AppCompatActivity  {
    SharedPreferences preferences;
    String token ="";
    TextView usernameProfile;
    String url="";
    int count = 0;
    String [] imglist = new String[50];
    String [] imgdescription = new String[50];


    private ImageView imageView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        usernameProfile=findViewById(R.id.usernameProfil);
        url=getResources().getString(R.string.ip_photo);
        count=0;


        imageView = (ImageView) findViewById(R.id.imageView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},121);
        preferences =getSharedPreferences("sharedpref",MODE_PRIVATE);
        token =preferences.getString("Token","");

        getProfilUser();

}

    public void getProfilUser(){

        String ipadd=getResources().getString(R.string.ip);

        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(ipadd)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserService user = retrofit.create(UserService.class);

        Call<Post> call=user.getprofile("Bearer " + token.replaceAll("\\s+",""));

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {


                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Failur",Toast.LENGTH_LONG).show();
                }


                if (response.isSuccessful()) {

                    Post posts = response.body();
                    usernameProfile.setText(posts.getUsername());


                    List<Post.PostsBean> list = posts.getPosts();
                    for (Post.PostsBean item : list){
                        imglist[count]="" + (url+item.getImg()).replaceAll("\\s+","");
                        imgdescription[count]=""+ item.getDescription();
                        count++;
                    }

                    ArrayList imageUrlList = prepareData();
                    DataAdapter dataAdapter = new DataAdapter(getApplicationContext(), imageUrlList);
                    recyclerView.setAdapter(dataAdapter);

                }

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

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
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                return true;
            case R.id.profile:
                Toast.makeText(this,"You are at profile",Toast.LENGTH_LONG).show();
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

    private ArrayList prepareData() {
        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ImageUrl imageUrl = new ImageUrl(imglist[i],imgdescription[i]);
            imageUrlList.add(imageUrl);
        }
        return imageUrlList;
    }

    public void logout(){
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.apply();

    }



}
