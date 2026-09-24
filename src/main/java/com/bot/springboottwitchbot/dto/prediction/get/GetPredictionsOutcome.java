package com.bot.springboottwitchbot.dto.prediction.get;

import com.bot.springboottwitchbot.dto.prediction.TopPredictors;

import java.util.ArrayList;

public class GetPredictionsOutcome {
    private String id;
    private String title;
    private int users;
    private int channel_points;
    private ArrayList<TopPredictors> top_predictors;
    private String color;

    public GetPredictionsOutcome() {
    }

    public GetPredictionsOutcome(String id, String title, int users, int channel_points, ArrayList<TopPredictors> top_predictors, String color) {
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

    public ArrayList<TopPredictors> getTop_predictors() {
        return top_predictors;
    }

    public void setTop_predictors(ArrayList<TopPredictors> top_predictors) {
        this.top_predictors = top_predictors;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "GetPredictionsOutcome{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", users=" + users +
                ", channel_points=" + channel_points +
                ", top_predictors=" + top_predictors +
                ", color='" + color + '\'' +
                '}';
    }
}
