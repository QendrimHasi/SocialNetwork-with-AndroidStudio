package com.example.windows.insta.model;

public class ImageUrl {

    String imageUrl;
    String description;


    public ImageUrl(String imageUrl, String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public ImageUrl() {
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
