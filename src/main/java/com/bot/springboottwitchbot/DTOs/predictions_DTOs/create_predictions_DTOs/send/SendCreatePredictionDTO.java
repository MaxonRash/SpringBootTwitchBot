package com.bot.springboottwitchbot.DTOs.predictions_DTOs.create_predictions_DTOs.send;

import java.util.ArrayList;

public class SendCreatePredictionDTO {
    private String broadcaster_id;
    private String title;
    private int prediction_window;
    private ArrayList<Outcomes> outcomes;

    public SendCreatePredictionDTO() {
    }

    public SendCreatePredictionDTO(String broadcaster_id, String title, int prediction_window, ArrayList<Outcomes> outcomes) {
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

    public ArrayList<Outcomes> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(ArrayList<Outcomes> outcomes) {
        this.outcomes = outcomes;
    }
}
