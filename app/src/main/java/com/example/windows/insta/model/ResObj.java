package com.example.windows.insta.model;

public class ResObj {

    private String username,password;
    private String message;

    public ResObj(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
