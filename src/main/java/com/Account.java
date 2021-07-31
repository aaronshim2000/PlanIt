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

    private String role; //role


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


    public String getRole()
    {
        return this.role;
    }

    public void setId(String s)
    {
        this.id = s;
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

    public void setRole(String s)
    {
        this.role = s;
    }
}