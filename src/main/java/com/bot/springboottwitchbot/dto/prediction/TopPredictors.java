package com.bot.springboottwitchbot.dto.prediction;

public class TopPredictors {
    private String name;

    public TopPredictors() {
    }

    public TopPredictors(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
