package com.app_services.WooNam.chattingapp.UserModel;

import android.util.Log;

public class User {//유저 변수명 정의
    private String id;
    private String name;
    private String username;
    private String email;
    private String school;
    private String git;
    private String aword;
    private String favorite;
    private String imageURL;
    private String user_about;
    private String status;
    private String searchable_name;

    public User(String id, String fName, String username, String email, String imageURL, String user_about, String user_status, String searchable_name,String git,String aword,String favorite,String school) {
        //유저 함수정의
        this.id = id;
        this.name = fName;
        this.username = username;
        this.email = email;
        this.imageURL = imageURL;
        this.user_about = user_about;
        this.status = user_status;
        this.searchable_name = searchable_name;
        this.git=git;
        this.favorite=favorite;
        this.aword=aword;
        this.school=school;

    }

    public User(){
        //유저 기본 상태 정의 ;
        imageURL = "default";
        user_about = "Hey there, I'm using Chatting App.";
        Log.i("UserModel", "User: Default Constructor without Parameter!");
    }
//profile에 들어갈 함수,변수정의
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }

    public String getFavorite() {
        return favorite;

    }
    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getAword() {
        return aword;

    }
    public void setAword(String aword) {
        this.aword = aword;
    }

    public String getGit() {
        return git;

    }
    public void setGit(String git) {
        this.git = git;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUser_about() {
        return user_about;
    }

    public void setUser_about(String user_about) {
        this.user_about = user_about;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearchable_name() {
        return searchable_name;
    }

    public void setSearchable_name(String searchable_name) {//검색시 필요한 함수정의
        this.searchable_name = searchable_name;
    }

}
