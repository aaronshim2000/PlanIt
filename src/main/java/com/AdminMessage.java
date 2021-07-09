package com.example;

public class AdminMessage
{
    private String id;
    private String username;
    private String email;
    private String message;
    private String category;

    public String getId()
    {
        return this.id;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getMessage()
    {
        return this.message;
    }

    public String getCategory()
    {
        return this.category;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setUsername(String s)
    {
        username = s;
    }

    public void setEmail(String s)
    {
        email = s;
    }

    public void setMessage(String s)
    {
        message = s;
    }

    public void setCategory(String s)
    {
        category = s;
    }
}
