package com.bot.springboottwitchbot.dto.bannedusers;

import java.util.ArrayList;

public class BannedUsersResponse {

    public BannedUsersResponse() {
    }

    private ArrayList<BannedUserData> data;
    private Object pagination;

    public BannedUsersResponse(ArrayList<BannedUserData> data) {
        this.data = data;
    }

    public ArrayList<BannedUserData> getData() {
        return data;
    }

    public void setData(ArrayList<BannedUserData> data) {
        this.data = data;
    }

    public Object getPagination() {
        return pagination;
    }

    public void setPagination(Object pagination) {
        this.pagination = pagination;
    }
}
