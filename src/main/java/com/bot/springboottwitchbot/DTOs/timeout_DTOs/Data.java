package com.bot.springboottwitchbot.DTOs.timeout_DTOs;

public class Data {
    private String user_id;
    private int duration;
    private String reason;

    public Data(String user_id, int duration, String reason) {
        this.user_id = user_id;
        this.duration = duration;
        this.reason = reason;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
