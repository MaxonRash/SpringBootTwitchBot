package com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs;

public class Outcomes {
    private String id;
    private String title;
    private int users;
    private int channel_points;
    private String top_predictors;
    private String color;

    public Outcomes() {
    }

    public Outcomes(String id, String title, int users, int channel_points, String top_predictors, String color) {
        this.id = id;
        this.title = title;
        this.users = users;
        this.channel_points = channel_points;
        this.top_predictors = top_predictors;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getChannel_points() {
        return channel_points;
    }

    public void setChannel_points(int channel_points) {
        this.channel_points = channel_points;
    }

    public String getTop_predictors() {
        return top_predictors;
    }

    public void setTop_predictors(String top_predictors) {
        this.top_predictors = top_predictors;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
