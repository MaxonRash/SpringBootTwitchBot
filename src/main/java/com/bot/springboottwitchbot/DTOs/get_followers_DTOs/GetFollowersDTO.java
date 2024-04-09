package com.bot.springboottwitchbot.DTOs.get_followers_DTOs;

import java.util.ArrayList;

public class GetFollowersDTO {
    private int total;
    private Object pagination;
    private ArrayList<Data> data;

    public GetFollowersDTO() {
    }

    public GetFollowersDTO(int total, Object pagination, ArrayList<Data> data) {
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

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
