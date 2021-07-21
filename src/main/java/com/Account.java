package com;

import com.example.Post;
import java.util.ArrayList;

public class Account
{
    private String id;

    private String username; //username

    private String password; //password

    private String email; //email address

    private String fname; //first name

    private String lname; //last name

    private ArrayList<Account> friendList=new ArrayList<Account>();

    private ArrayList<Post> postHistory=new ArrayList<Post>();

    public String getId()
    {
        return this.id;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getFname()
    {
        return this.fname;
    }

    public String getLname()
    {
        return this.lname;
    }

    public ArrayList<Account> getFriendList(){
        return this.friendList;
    }

    public ArrayList<Post> getPostHistory(){
        return this.postHistory;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setUsername(String s)
    {
        this.username = s;
    }

    public void setPassword(String s)
    {
        this.password = s;
    }

    public void setEmail(String s)
    {
        this.email = s;
    }
    
    public void setFname(String s)
    {
        this.fname = s;
    }

    public void setLname(String s)
    {
        this.lname = s;
    }

    public void setFriend(ArrayList<Account> a){
        this.friendList=a;
    }

    public void setPostHistory(ArrayList<Post> p){
        this.postHistory=p;
    }

    public void addFriend(Account a){
        friendList.add(a);
    }

    public void addPostHistory(Post p){
        postHistory.add(p);
    }
}