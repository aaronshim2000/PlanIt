package com.example;

enum Category {
    NORMAL, REVIEW, PLAN
}

public class Post
{
    private String title;
    private String description;
    private Category category;

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Type getCategory()
    {
        return this.category;
    }

    public void setTitle(String s)
    {
        title = s;
    }

    public void setDescription(String s)
    {
        description = s;
    }

    public void setCategory(Category c)
    {
        category = c;
    }
}
