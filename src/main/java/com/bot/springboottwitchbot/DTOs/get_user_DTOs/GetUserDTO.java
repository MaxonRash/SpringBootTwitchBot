package com.bot.springboottwitchbot.DTOs.get_user_DTOs;

import java.util.ArrayList;

public class GetUserDTO {
    private ArrayList<Data> data;

    public GetUserDTO() {
    }

    public GetUserDTO(ArrayList<Data> data) {
        this.data = data;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
