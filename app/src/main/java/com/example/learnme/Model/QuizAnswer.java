package com.example.learnme.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuizAnswer implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_quiz_question")
    @Expose
    private String idQuizQuestion;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("checking")
    @Expose
    private String checking;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("point")
    @Expose
    private String point;

    public QuizAnswer(String id, String idQuizQuestion, String answer, String checking, String description, String point) {
        this.id = id;
        this.idQuizQuestion = idQuizQuestion;
        this.answer = answer;
        this.checking = checking;
        this.description = description;
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdQuizQuestion() {
        return idQuizQuestion;
    }

    public void setIdQuizQuestion(String idQuizQuestion) {
        this.idQuizQuestion = idQuizQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getChecking() {
        return checking;
    }

    public void setChecking(String checking) {
        this.checking = checking;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
