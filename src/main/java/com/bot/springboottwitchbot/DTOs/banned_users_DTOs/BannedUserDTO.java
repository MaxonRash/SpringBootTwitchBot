package com.bot.springboottwitchbot.DTOs.banned_users_DTOs;

import java.util.ArrayList;

public class BannedUserDTO {

    public BannedUserDTO() {
    }

    private ArrayList<Data> data;
    private Object pagination;

    public BannedUserDTO(ArrayList<Data> data) {
        this.data = data;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public Object getPagination() {
        return pagination;
    }

    public void setPagination(Object pagination) {
        this.pagination = pagination;
    }
}
