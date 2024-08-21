package com.bot.springboottwitchbot.DTOs.predictions_DTOs;

public class Top_predictors {
    private String name;

    public Top_predictors() {
    }

    public Top_predictors(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
