package com.example;

enum PostCategory {
    NORMAL, REVIEW, PLAN
}

public class Post
{
    private String title;
    private String description;
    private PostCategory category;

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public PostCategory getCategory()
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

    public void setCategory(PostCategory c)
    {
        category = c;
    }
}
