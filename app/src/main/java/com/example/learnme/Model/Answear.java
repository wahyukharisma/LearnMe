package com.example.learnme.Model;

public class Answear {
    String name;
    String answear;
    String date;
    Integer like;
    Integer dislike;

    public Answear(String name, String answear, String date, Integer like, Integer dislike) {
        this.name = name;
        this.answear = answear;
        this.date = date;
        this.like = like;
        this.dislike = dislike;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswear() {
        return answear;
    }

    public void setAnswear(String answear) {
        this.answear = answear;
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
}
