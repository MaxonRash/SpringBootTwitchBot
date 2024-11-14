package com.bot.springboottwitchbot.DTOs.banned_users_DTOs;

public class Data {
    private String user_id;
    private String user_login;
    private String user_name;
    private String expires_at;
    private String created_at;
    private String reason;
    private String moderator_id;
    private String moderator_login;
    private String moderator_name;

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

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModerator_id() {
        return moderator_id;
    }

    public void setModerator_id(String moderator_id) {
        this.moderator_id = moderator_id;
    }

    public String getModerator_login() {
        return moderator_login;
    }

    public void setModerator_login(String moderator_login) {
        this.moderator_login = moderator_login;
    }

    public String getModerator_name() {
        return moderator_name;
    }

    public void setModerator_name(String moderator_name) {
        this.moderator_name = moderator_name;
    }
}
