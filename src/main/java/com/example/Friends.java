package com.example;


import com.example.Post;
import java.util.ArrayList;


public class Friends 
{
    private String id;
    private String user1;
    private String user2;
    private Boolean isFriends;
    private String date;

    public String getId(){
        return this.id;
    }

    public String getUser1(){
        return this.user1;
    }

    public String getUser2(){
        return this.user2;
    }

    public Boolean getIsFriends(){
        return this.isFriends;
    }

    public String getDate(){
        return this.date;
    }

    public void setId(String s){
        id = s;
    }

    public void setUser1(String s){
        user1 = s;
    }

    public void setUser2(String s){
        user2 = s;
    }

    public void setIsFriends(Boolean b){
        isFriends = b;
    }

    public void setDate(String s){
        date = s;
    }
}