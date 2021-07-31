package com;

import com.example.Post;
import java.util.ArrayList;

public class Notification
{
    private String id;
    private String title;
    private String sender;
    private String recipient;
    private String body;
    private String time;

    //getters
    public String getId(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public String getSender(){
        return this.sender;
    }
    public String getRecipient(){
        return this.recipient;
    }
    public String getBody(){
        return this.body;
    }
    public String getTime(){
        return this.time;
    }

    //setters
    public void setId(String s){
        this.id = s;
    }
    public void setTitle(String s){
        this.title = s;
    }
    public void setSender(String s){
        this.sender = s;
    }
    public void setRecipient(String s){
        this.recipient = s;
    }
    public void setBody(String s){
        this.body = s;
    }
    public void setTime(String s){
        this.time = s;
    }
}