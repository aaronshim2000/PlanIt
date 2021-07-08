package com.example;

public class User
{
    private String username; //username
    private String password; //password
    private String email; //email address
    private String fname; //first name
    private String lname; //last name

    public String getUser()
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


    public void setUser(String s)
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
}