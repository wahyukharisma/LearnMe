package com.example.learnme.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuizQuestion implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_quiz")
    @Expose
    private String idQuiz;
    @SerializedName("question")
    @Expose
    private String question;

    public QuizQuestion(String id, String idQuiz, String question) {
        this.id = id;
        this.idQuiz = idQuiz;
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(String idQuiz) {
        this.idQuiz = idQuiz;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
