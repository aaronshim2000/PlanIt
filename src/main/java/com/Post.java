package com.example;

enum Type {
    NORMAL, REVIEW, PLAN
}

public class Post
{
    private String title;
    private String description;
    private Type type;

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
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
