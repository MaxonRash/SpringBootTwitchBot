package com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs;

import java.util.ArrayList;

public class Data {
    private String id;
    private String broadcaster_id;
    private String broadcaster_name;
    private String broadcaster_login;
    private String title;
    private String winning_outcome_id;
    private ArrayList<Outcomes> outcomes;
    private int prediction_window;
    private String status;
    private String created_at;
    private String ended_at;
    private String locked_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBroadcaster_id() {
        return broadcaster_id;
    }

    public void setBroadcaster_id(String broadcaster_id) {
        this.broadcaster_id = broadcaster_id;
    }

    public String getBroadcaster_name() {
        return broadcaster_name;
    }

    public void setBroadcaster_name(String broadcaster_name) {
        this.broadcaster_name = broadcaster_name;
    }

    public String getBroadcaster_login() {
        return broadcaster_login;
    }

    public void setBroadcaster_login(String broadcaster_login) {
        this.broadcaster_login = broadcaster_login;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWinning_outcome_id() {
        return winning_outcome_id;
    }

    public void setWinning_outcome_id(String winning_outcome_id) {
        this.winning_outcome_id = winning_outcome_id;
    }

    public ArrayList<Outcomes> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(ArrayList<Outcomes> outcomes) {
        this.outcomes = outcomes;
    }

    public int getPrediction_window() {
        return prediction_window;
    }

    public void setPrediction_window(int prediction_window) {
        this.prediction_window = prediction_window;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEnded_at() {
        return ended_at;
    }

    public void setEnded_at(String ended_at) {
        this.ended_at = ended_at;
    }

    public String getLocked_at() {
        return locked_at;
    }

    public void setLocked_at(String locked_at) {
        this.locked_at = locked_at;
    }

    public Data(String id, String broadcaster_id, String broadcaster_name, String broadcaster_login, String title, String winning_outcome_id, ArrayList<Outcomes> outcomes, int prediction_window, String status, String created_at, String ended_at, String locked_at) {
        this.id = id;
        this.broadcaster_id = broadcaster_id;
        this.broadcaster_name = broadcaster_name;
        this.broadcaster_login = broadcaster_login;
        this.title = title;
        this.winning_outcome_id = winning_outcome_id;
        this.outcomes = outcomes;
        this.prediction_window = prediction_window;
        this.status = status;
        this.created_at = created_at;
        this.ended_at = ended_at;
        this.locked_at = locked_at;
    }

    public Data() {
    }

    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", broadcaster_id='" + broadcaster_id + '\'' +
                ", broadcaster_name='" + broadcaster_name + '\'' +
                ", broadcaster_login='" + broadcaster_login + '\'' +
                ", title='" + title + '\'' +
                ", winning_outcome_id='" + winning_outcome_id + '\'' +
                ", outcomes=" + outcomes +
                ", prediction_window=" + prediction_window +
                ", status='" + status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", ended_at='" + ended_at + '\'' +
                ", locked_at='" + locked_at + '\'' +
                '}';
    }
}
