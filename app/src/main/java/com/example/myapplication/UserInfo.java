package com.example.myapplication;

//this class is used to store the user information
public class UserInfo {

    public String username;
    public String email;
    public UserInfo(){}
    public UserInfo(String username, String email){
        this.username = username;
        this.email = email;
    }
    public String getUsername(){
        return this.username;
    }

}
