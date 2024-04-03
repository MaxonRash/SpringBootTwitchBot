package com.bot.springboottwitchbot.DTOs.emote_only_DTOs;

public class EmoteOnlyDTO {
    private Boolean emote_mode;

    public EmoteOnlyDTO() {
    }

    public EmoteOnlyDTO(Boolean emote_mode) {
        this.emote_mode = emote_mode;
    }

    public Boolean getEmote_mode() {
        return emote_mode;
    }

    public void setEmote_mode(Boolean emote_mode) {
        this.emote_mode = emote_mode;
    }
}
