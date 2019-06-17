package com.example.learnme.Model;

public class TempAnswer {
    String index;
    String answer;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public TempAnswer(String index, String answer) {
        this.index = index;
        this.answer = answer;
    }
}
