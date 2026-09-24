package com.bot.springboottwitchbot.dto.user;

import java.util.ArrayList;

public class UsersResponse {
    private ArrayList<UserData> data;

    public UsersResponse() {
    }

    public UsersResponse(ArrayList<UserData> data) {
        this.data = data;
    }

    public ArrayList<UserData> getData() {
        return data;
    }

    public void setData(ArrayList<UserData> data) {
        this.data = data;
    }
}
