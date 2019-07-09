package com.example.windows.insta.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostOfFollowers {



    @SerializedName("post_id")
    private int post_id;
    @SerializedName("description")
    private String description;
    @SerializedName("img")
    private String img;
    @SerializedName("user_id")
    private int user_id;

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public static class PostHome{
        private List<PostOfFollowers> posts;

        public List<PostOfFollowers> getPosts() {
            return posts;
        }

        public void setPosts(List<PostOfFollowers> posts) {
            this.posts = posts;
        }
    }
}
