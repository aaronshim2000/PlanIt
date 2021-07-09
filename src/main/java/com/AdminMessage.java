package com.example;

enum MessageCategory {
    CONTACT, REPORT
}

public class AdminMessage
{
    private String username;
    private String email;
    private String message;
    private MessageCategory category;

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

    public MessageCategory getCategory()
    {
        return this.category;
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

    public void setCategory(MessageCategory c)
    {
        category = c;
    }
}
