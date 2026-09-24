package com.bot.springboottwitchbot.dto.prediction.get;

import java.util.ArrayList;

public class GetPredictionsResponse {
    private ArrayList<GetPredictionsData> data;
    private Object pagination;

    public GetPredictionsResponse() {
    }

    public GetPredictionsResponse(ArrayList<GetPredictionsData> data, Object pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public ArrayList<GetPredictionsData> getData() {
        return data;
    }

    public void setData(ArrayList<GetPredictionsData> data) {
        this.data = data;
    }

    public Object getPagination() {
        return pagination;
    }

    public void setPagination(Object pagination) {
        this.pagination = pagination;
    }
}
