package com.example.learnme.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Point {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("ponit")
    @Expose
    private String ponit;
    @SerializedName("point_calculation")
    @Expose
    private String pointCalculation;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("description")
    @Expose
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPonit() {
        return ponit;
    }

    public void setPonit(String ponit) {
        this.ponit = ponit;
    }

    public String getPointCalculation() {
        return pointCalculation;
    }

    public void setPointCalculation(String pointCalculation) {
        this.pointCalculation = pointCalculation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
