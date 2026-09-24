package com.bot.springboottwitchbot.dto.emoteonly;

public class EmoteOnlyRequest {
    private Boolean emote_mode;

    public EmoteOnlyRequest() {
    }

    public EmoteOnlyRequest(Boolean emote_mode) {
        this.emote_mode = emote_mode;
    }

    public Boolean getEmote_mode() {
        return emote_mode;
    }

    public void setEmote_mode(Boolean emote_mode) {
        this.emote_mode = emote_mode;
    }
}
