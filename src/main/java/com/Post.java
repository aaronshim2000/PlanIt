package com.example;

enum Type {
    TEXT, REVIEW, PLANNING
}

public class Post
{
    private String title;
    private String description;
    private Type type;

    public String getTitle()
    {
        return this.user;
    }

    public String getDescription()
    {
        return this.password;
    }

    public String getType()
    {
        return this.type;
    }

    public void setTitle(String s)
    {
        title = s;
    }

    public void setDescription(String s)
    {
        description = s;
    }

    public void setType(Type t)
    {
        type = t;
    }
}