package com.bot.springboottwitchbot.DTOs.predictions_DTOs.create_predictions_DTOs.send;

public class Outcomes {
    private String title;

    public Outcomes() {
    }

    public Outcomes(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
