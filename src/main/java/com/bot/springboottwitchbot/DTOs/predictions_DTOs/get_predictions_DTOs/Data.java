package com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs;

import java.util.ArrayList;

public class Data {
    private String id;
    private String broadcaster_id;
    private String broadcaster_name;
    private String broadcaster_login;
    private String title;
    private String winning_outcome_id;
    private ArrayList<Outcomes> outcomes;
    private int prediction_window;
    private String status;
    private String created_at;
    private String ended_at;
    private String locked_at;
}
