package com.bot.springboottwitchbot.dto.prediction.create.request;

public class CreatePredictionRequestOutcome {
    private String title;

    public CreatePredictionRequestOutcome() {
    }

    public CreatePredictionRequestOutcome(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
