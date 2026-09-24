package com.bot.springboottwitchbot.dto.prediction.create.request;

import java.util.ArrayList;

public class CreatePredictionRequest {
    private String broadcaster_id;
    private String title;
    private int prediction_window;
    private ArrayList<CreatePredictionRequestOutcome> outcomes;

    public CreatePredictionRequest() {
    }

    public CreatePredictionRequest(String broadcaster_id, String title, int prediction_window, ArrayList<CreatePredictionRequestOutcome> outcomes) {
        this.broadcaster_id = broadcaster_id;
        this.title = title;
        this.prediction_window = prediction_window;
        this.outcomes = outcomes;
    }

    public String getBroadcaster_id() {
        return broadcaster_id;
    }

    public void setBroadcaster_id(String broadcaster_id) {
        this.broadcaster_id = broadcaster_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrediction_window() {
        return prediction_window;
    }

    public void setPrediction_window(int prediction_window) {
        this.prediction_window = prediction_window;
    }

    public ArrayList<CreatePredictionRequestOutcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(ArrayList<CreatePredictionRequestOutcome> outcomes) {
        this.outcomes = outcomes;
    }
}
