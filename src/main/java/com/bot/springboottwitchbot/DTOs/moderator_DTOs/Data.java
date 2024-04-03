package com.bot.springboottwitchbot.DTOs.moderator_DTOs;

public class Data {
    private String user_id;
    private String user_login;
    private String user_name;

    public Data(String user_id, String user_login, String user_name) {
        this.user_id = user_id;
        this.user_login = user_login;
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
