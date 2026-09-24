package com.bot.springboottwitchbot.dto.timeout;


public class TimeoutUserRequest {
    private TimeoutData data;

    public TimeoutUserRequest() {
    }

    public TimeoutUserRequest(TimeoutData data) {
        this.data = data;
    }

    public TimeoutData getData() {
        return data;
    }

    public void setData(TimeoutData data) {
        this.data = data;
    }

}
