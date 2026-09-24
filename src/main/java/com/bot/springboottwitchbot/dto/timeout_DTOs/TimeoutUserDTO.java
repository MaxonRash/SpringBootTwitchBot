package com.bot.springboottwitchbot.dto.timeout_DTOs;


public class TimeoutUserDTO {
    private Data data;

    public TimeoutUserDTO() {
    }

    public TimeoutUserDTO(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
