package com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs;

import com.bot.springboottwitchbot.DTOs.get_followers_DTOs.Data;

import java.util.ArrayList;

public class GetPredictionsDTO {
    private ArrayList<Data> data;
    private Object pagination;

    public GetPredictionsDTO() {
    }

    public GetPredictionsDTO(ArrayList<Data> data, Object pagination) {
        this.data = data;
        this.pagination = pagination;
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
