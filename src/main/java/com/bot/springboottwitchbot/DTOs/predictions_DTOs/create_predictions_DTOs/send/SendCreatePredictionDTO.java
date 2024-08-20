package com.bot.springboottwitchbot.DTOs.predictions_DTOs.create_predictions_DTOs.send;

import java.util.ArrayList;

public class SendCreatePredictionDTO {
    private String broadcaster_id;
    private String title;
    private int prediction_window;
    private ArrayList<Outcomes> outcomes;

}
