package com.bot.springboottwitchbot.dto.moderator;

import java.util.ArrayList;

public class ModeratorsResponse {

    public ModeratorsResponse() {
    }

    private ArrayList<ModeratorData> data;

    public ModeratorsResponse(ArrayList<ModeratorData> data) {
        this.data = data;
    }

    public ArrayList<ModeratorData> getData() {
        return data;
    }

    public void setData(ArrayList<ModeratorData> data) {
        this.data = data;
    }
}
