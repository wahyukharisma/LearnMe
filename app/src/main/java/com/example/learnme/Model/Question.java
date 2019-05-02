package com.example.learnme.Model;

public class Question {
    String title;
    String description;
    String date;
    Integer like;
    Integer dislike;
    Integer comment;

    public Question(String title, String description, String date, Integer like, Integer dislike, Integer comment) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.like = like;
        this.dislike = dislike;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getDislike() {
        return dislike;
    }

    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }
}
