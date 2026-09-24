package com.bot.springboottwitchbot.dto.prediction.end.response;

import java.util.ArrayList;

public class EndPredictionResponse {
    ArrayList<EndPredictionResponseData> data;

    public EndPredictionResponse(ArrayList<EndPredictionResponseData> data) {
        this.data = data;
    }

    public EndPredictionResponse() {
    }

    public ArrayList<EndPredictionResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<EndPredictionResponseData> data) {
        this.data = data;
    }
}
