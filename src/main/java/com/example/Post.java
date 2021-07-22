package com.example;

import com.Account;

public class Post
{

    private String title;
    private String description;
    private String category;
    private String visibility;
    private String rating;
    private String creator;

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getCategory()
    {
        return this.category;
    }

    public String getVisibility(){
        return this.visibility;
    }
    public String getRating(){
        return this.rating;
    }
    public String getCreator(){
        return this.creator;
    }

    public void setTitle(String s)
    {
        title = s;
    }

    public void setDescription(String s)
    {
        description = s;
    }

    public void setCategory(String s)
    {
        category = s;
    }

    public void setVisibility(String s){
        visibility=s;
    }
    public void setRating(String s){
        rating=s;
    }
    public void setCreator(String s){
        creator=s;
    }
}
