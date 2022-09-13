package ru.otus.spring.domain;

public class Question {

    private String questionValue;
    private String answerValue;

    public Question(String questionValue, String answerValue) {
        this.questionValue = questionValue;
        this.answerValue = answerValue;
    }

    public String getQuestionValue() {
        return questionValue;
    }

    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }
}
