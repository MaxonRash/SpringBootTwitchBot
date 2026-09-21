package com.bot.springboottwitchbot.gpt;

public enum TsyaMode {
    ON("ON"), OFF("OFF");

    private final String name;

    TsyaMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
