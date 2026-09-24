package com.bot.springboottwitchbot.dto.prediction.create.response;

import java.util.ArrayList;

public class CreatePredictionResponse {
    ArrayList<CreatePredictionResponseData> data;

    public CreatePredictionResponse() {
    }

    public CreatePredictionResponse(ArrayList<CreatePredictionResponseData> data) {
        this.data = data;
    }

    public ArrayList<CreatePredictionResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<CreatePredictionResponseData> data) {
        this.data = data;
    }
}
