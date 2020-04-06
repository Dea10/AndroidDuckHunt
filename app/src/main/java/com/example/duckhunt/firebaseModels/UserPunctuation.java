package com.example.duckhunt.firebaseModels;

public class UserPunctuation {

    private String userName;
    private Integer finalScore;

    public UserPunctuation() {}

    public UserPunctuation(String userName, Integer finalScore) {
        this.userName = userName;
        this.finalScore = finalScore;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }
}
