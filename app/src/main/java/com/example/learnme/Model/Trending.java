package com.example.learnme.Model;

public class Trending {
    Integer id;
    String Title;
    String Description;
    Integer Like;
    Integer Dislike;
    Integer Comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Trending(Integer id, String title, String description, Integer like, Integer dislike, Integer comment) {
        this.id = id;
        Title = title;
        Description = description;
        Like = like;
        Dislike = dislike;
        Comment = comment;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Integer getLike() {
        return Like;
    }

    public void setLike(Integer like) {
        Like = like;
    }

    public Integer getDislike() {
        return Dislike;
    }

    public void setDislike(Integer dislike) {
        Dislike = dislike;
    }

    public Integer getComment() {
        return Comment;
    }

    public void setComment(Integer comment) {
        Comment = comment;
    }
}
