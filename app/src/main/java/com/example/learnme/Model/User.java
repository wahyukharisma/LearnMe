package com.example.learnme.Model;

public class User {
    String name;
    int point;
    int id;

    public User(String name, int point, int id) {
        this.name = name;
        this.point = point;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
