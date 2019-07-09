package com.example.windows.insta.model;

public class File_info {

    private String description;
    private String img;

    public File_info(String description, String img) {
        this.description = description;
        this.img = img;
    }
    public File_info() {
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
}
