package com.bot.springboottwitchbot.DTOs.predictions_DTOs.create_predictions_DTOs.receive;

import java.util.ArrayList;

public class ReceiveCreatePredictionDTO {
    ArrayList<Data> data;

    public ReceiveCreatePredictionDTO() {
    }

    public ReceiveCreatePredictionDTO(ArrayList<Data> data) {
        this.data = data;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
