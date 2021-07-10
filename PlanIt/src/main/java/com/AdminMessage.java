package com.example;

enum MessageCategory {
    CONTACT, REPORT
}

public class AdminMessage
{
    private String user;
    private String email;
    private String message;
    private MessageCategory category;

    public String getUser()
    {
        return this.user;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getMessage()
    {
        return this.message;
    }

    public MessageCategory getCategory()
    {
        return this.category;
    }

    public void setUser(String s)
    {
        user = s;
    }

    public void setEmail(String s)
    {
        email = s;
    }

    public void setMessage(String s)
    {
        message = s;
    }

    public void setCategory(MessageCategory c)
    {
        category = c;
    }
}
