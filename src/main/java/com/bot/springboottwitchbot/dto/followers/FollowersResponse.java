package com.bot.springboottwitchbot.dto.followers;

import java.util.ArrayList;

public class FollowersResponse {
    private int total;
    private Object pagination;
    private ArrayList<FollowerData> data;

    public FollowersResponse() {
    }

    public FollowersResponse(int total, Object pagination, ArrayList<FollowerData> data) {
        this.total = total;
        this.pagination = pagination;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Object getPagination() {
        return pagination;
    }

    public void setPagination(Object pagination) {
        this.pagination = pagination;
    }

    public ArrayList<FollowerData> getData() {
        return data;
    }

    public void setData(ArrayList<FollowerData> data) {
        this.data = data;
    }
}
