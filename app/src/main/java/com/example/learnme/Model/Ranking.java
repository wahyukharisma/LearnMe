package com.example.learnme.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ranking {
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("point")
    @Expose
    private String point;
    @SerializedName("username")
    @Expose
    private String username;

    public Ranking(Integer index, String point, String username) {
        this.index = index;
        this.point = point;
        this.username = username;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
