package com.bot.springboottwitchbot.DTOs.predictions_DTOs.end_predictions_DTOs.receive;

import java.util.ArrayList;

public class ReceiveEndPredictionDTO {
    ArrayList<Data> data;

    public ReceiveEndPredictionDTO(ArrayList<Data> data) {
        this.data = data;
    }

    public ReceiveEndPredictionDTO() {
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
