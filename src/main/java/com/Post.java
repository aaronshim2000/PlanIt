package com.example;

enum PostCategory {
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

    public Category getCategory()
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
