package com.bot.springboottwitchbot.DTOs.moderator_DTOs;

import java.util.ArrayList;

public class ModeratorDTO {

    public ModeratorDTO() {
    }

    private ArrayList<Data> data;

    public ModeratorDTO(ArrayList<Data> data) {
        this.data = data;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
