package com.bot.springboottwitchbot.dto.prediction.end.request;

public class EndPredictionRequest {
    private String broadcaster_id;
    private String id;
    private String status;
    private String winning_outcome_id;

    public EndPredictionRequest() {
    }

    public EndPredictionRequest(String broadcaster_id, String id, String status, String winning_outcome_id) {
        this.broadcaster_id = broadcaster_id;
        this.id = id;
        this.status = status;
        this.winning_outcome_id = winning_outcome_id;
    }

    public String getBroadcaster_id() {
        return broadcaster_id;
    }

    public void setBroadcaster_id(String broadcaster_id) {
        this.broadcaster_id = broadcaster_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWinning_outcome_id() {
        return winning_outcome_id;
    }

    public void setWinning_outcome_id(String winning_outcome_id) {
        this.winning_outcome_id = winning_outcome_id;
    }
}
