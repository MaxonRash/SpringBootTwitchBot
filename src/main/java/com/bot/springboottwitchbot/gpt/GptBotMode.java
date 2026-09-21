package com.bot.springboottwitchbot.gpt;

public enum GptBotMode {
    ON("ON"), OFF("OFF");

    private final String name;

    GptBotMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
