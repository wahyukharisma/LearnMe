package com.example.learnme.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MRanking {
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("point")
    @Expose
    private String point;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("username")
    @Expose
    private String username;

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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
